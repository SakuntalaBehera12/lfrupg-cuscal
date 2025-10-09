package com.liferay.portlet.journal.asset;

import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.model.JournalArticle;

public class CuscalJournalArticleAssetRenderer {

    public static long getClassPK(JournalArticle article) {
        if ((article.isDraft() || article.isPending()) &&
                (article.getVersion() != JournalArticleConstants.VERSION_DEFAULT)) {

            return article.getPrimaryKey();
        }

        return article.getResourcePrimKey();
    }

}
