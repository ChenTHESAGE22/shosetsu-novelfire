package eu.kanade.tachiyomi.extension.all.novelfire

import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document

class Novelfire : ParsedHttpSource() {

    override val name = "Novelfire"
    override val baseUrl = "https://novelfire.net"
    // Multi-language ready: use "all" to indicate the extension doesn't restrict to one language
    override val lang = "all"
    override val supportsLatest = true

    // --- Popular / Latest (basic implementations - optional for Shosetsu)
    override fun popularMangaSelector() = "div.book-item, div.book-card, article.book"
    override fun popularMangaFromElement(element: Element) = SManga.create().apply {
        // Some sites use <a> around the card, others use data attributes. Use fallbacks.
        title = element.selectFirst("h3.book-title, h2.title, .book-title")?.text() ?: element.text()
        url = element.selectFirst("a")?.attr("href") ?: element.selectFirst("a[href]")?.attr("href") ?: ""
        if (url.startsWith("/")) url = url
        thumbnail_url = element.selectFirst("img")?.attr("src") ?: ""
    }
    override fun popularMangaNextPageSelector() = "a.next, li.next a"

    override fun latestUpdatesSelector() = popularMangaSelector()
    override fun latestUpdatesFromElement(element: Element) = popularMangaFromElement(element)
    override fun latestUpdatesNextPageSelector() = popularMangaNextPageSelector()

    // --- Search (best-effort)
    override fun searchMangaSelector() = "div.book-item, div.book-card, article.book"
    override fun searchMangaFromElement(element: Element) = popularMangaFromElement(element)
    override fun searchMangaNextPageSelector() = popularMangaNextPageSelector()
    override fun getFilterList() = emptyList<com.github.salomonbrys.kotson.TypeToken<Any>>() // No filters

    // --- Chapter list and pages
    override fun chapterListSelector() = "ul.chapter-list li a, div.chapters a, .chapter-list a"
    override fun chapterFromElement(element: Element) = SChapter.create().apply {
        name = element.text().trim()
        url = element.attr("href")
        if (url.startsWith("/")) url = url
        // try to extract a chapter date or number if available
        date_upload = 0L
    }

    override fun pageListParse(document: Document): List<Page> {
        // Novelfire often uses a single main container for chapter text.
        val content = document.selectFirst("div.chapter-content, div.entry-content, .read-content, article .content")
        if (content != null) {
            // We'll return the whole HTML as a single "page" so Shosetsu/Tachiyomi can display it.
            // Page constructor: Page(index, imageUrl, imageUrl?) - for text we embed HTML into the imageUrl field (some clients accept it).
            // Safer: return a Page with empty image url and use chapter.text for text readers.
            val text = content.html()
            // We'll store the HTML inside a data URL-like pseudo-field (not standard). Clients that expect plain text may need adjustment.
            return listOf(Page(0, "", text))
        }
        // fallback: try paragraphs
        val paras = document.select("div#content p, div.chapter p, article p")
        if (paras.isNotEmpty()) {
            val text = paras.joinToString("\n\n") { it.text() }
            return listOf(Page(0, "", text))
        }
        return emptyList()
    }

    // --- Utility
    override fun mangaDetailsParse(document: Document): SManga {
        val manga = SManga.create()
        // title
        manga.title = document.selectFirst("h1.book-title, h1.title, .book-header h1")?.text() ?: ""
        // author - try common selectors
        val author = document.selectFirst(".author a, .book-author, .meta-author")?.text() ?: ""
        if (author.isNotBlank()) manga.author = author
        // description
        manga.description = document.selectFirst("div.description, .book-desc, .summary, .entry-content .summary")?.text() ?: ""
        // thumbnail
        manga.thumbnail_url = document.selectFirst("img.cover, .book-cover img, .cover img")?.attr("src") ?: ""
        return manga
    }

    // The following methods are required by ParsedHttpSource but are not used for this simple scraper.
    override fun pageListSelector() = throw UnsupportedOperationException("Not used")
    override fun imageUrlParse(document: Document) = throw UnsupportedOperationException("Not used")
    override fun popularMangaRequest(page: Int) = throw UnsupportedOperationException("Not used")
    override fun latestUpdatesRequest(page: Int) = throw UnsupportedOperationException("Not used")
    override fun searchMangaRequest(page: Int, query: String, filters: List<com.github.salomonbrys.kotson.TypeToken<Any>>) = throw UnsupportedOperationException("Not used")
}
