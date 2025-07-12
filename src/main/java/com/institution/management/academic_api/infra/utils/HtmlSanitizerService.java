package com.institution.management.academic_api.infra.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizerService {

    public String sanitize(String dirtyHtml) {
        if (dirtyHtml == null || dirtyHtml.isBlank()) {
            return null;
        }

        Safelist safelist = Safelist.basicWithImages()
                .addTags("h1", "h2", "h3", "h4", "h5", "h6", "table", "thead", "tbody", "tfoot", "tr", "td", "th")
                .addAttributes("table", "border", "cellpadding", "cellspacing")
                .addAttributes("td", "colspan", "rowspan"
        );

        Document.OutputSettings outputSettings = new Document.OutputSettings();

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        outputSettings.prettyPrint(false);

        return Jsoup.clean(dirtyHtml, "", safelist, outputSettings);
    }
}