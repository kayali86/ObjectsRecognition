package com.kayali_developer.googlecustomsearchlibrary.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    private String kind;
    private Url url;
    private Queries queries;
    private Context context;
    private SearchInformation searchInformation;
    private List<Item> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Queries getQueries() {
        return queries;
    }

    public void setQueries(Queries queries) {
        this.queries = queries;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SearchInformation getSearchInformation() {
        return searchInformation;
    }

    public void setSearchInformation(SearchInformation searchInformation) {
        this.searchInformation = searchInformation;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Url {

        private String type;
        private String template;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }
    }

    public static class Queries {
        private List<RequestBean> request;
        private List<NextPageBean> nextPage;

        public List<RequestBean> getRequest() {
            return request;
        }

        public void setRequest(List<RequestBean> request) {
            this.request = request;
        }

        public List<NextPageBean> getNextPage() {
            return nextPage;
        }

        public void setNextPage(List<NextPageBean> nextPage) {
            this.nextPage = nextPage;
        }

        public static class RequestBean {

            private String title;
            private String totalResults;
            private String searchTerms;
            private int count;
            private int startIndex;
            private String inputEncoding;
            private String outputEncoding;
            private String safe;
            private String cx;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTotalResults() {
                return totalResults;
            }

            public void setTotalResults(String totalResults) {
                this.totalResults = totalResults;
            }

            public String getSearchTerms() {
                return searchTerms;
            }

            public void setSearchTerms(String searchTerms) {
                this.searchTerms = searchTerms;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public void setStartIndex(int startIndex) {
                this.startIndex = startIndex;
            }

            public String getInputEncoding() {
                return inputEncoding;
            }

            public void setInputEncoding(String inputEncoding) {
                this.inputEncoding = inputEncoding;
            }

            public String getOutputEncoding() {
                return outputEncoding;
            }

            public void setOutputEncoding(String outputEncoding) {
                this.outputEncoding = outputEncoding;
            }

            public String getSafe() {
                return safe;
            }

            public void setSafe(String safe) {
                this.safe = safe;
            }

            public String getCx() {
                return cx;
            }

            public void setCx(String cx) {
                this.cx = cx;
            }
        }

        public static class NextPageBean {

            private String title;
            private String totalResults;
            private String searchTerms;
            private int count;
            private int startIndex;
            private String inputEncoding;
            private String outputEncoding;
            private String safe;
            private String cx;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTotalResults() {
                return totalResults;
            }

            public void setTotalResults(String totalResults) {
                this.totalResults = totalResults;
            }

            public String getSearchTerms() {
                return searchTerms;
            }

            public void setSearchTerms(String searchTerms) {
                this.searchTerms = searchTerms;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public void setStartIndex(int startIndex) {
                this.startIndex = startIndex;
            }

            public String getInputEncoding() {
                return inputEncoding;
            }

            public void setInputEncoding(String inputEncoding) {
                this.inputEncoding = inputEncoding;
            }

            public String getOutputEncoding() {
                return outputEncoding;
            }

            public void setOutputEncoding(String outputEncoding) {
                this.outputEncoding = outputEncoding;
            }

            public String getSafe() {
                return safe;
            }

            public void setSafe(String safe) {
                this.safe = safe;
            }

            public String getCx() {
                return cx;
            }

            public void setCx(String cx) {
                this.cx = cx;
            }
        }
    }

    public static class Context {

        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class SearchInformation {

        private double searchTime;
        private String formattedSearchTime;
        private String totalResults;
        private String formattedTotalResults;

        public double getSearchTime() {
            return searchTime;
        }

        public void setSearchTime(double searchTime) {
            this.searchTime = searchTime;
        }

        public String getFormattedSearchTime() {
            return formattedSearchTime;
        }

        public void setFormattedSearchTime(String formattedSearchTime) {
            this.formattedSearchTime = formattedSearchTime;
        }

        public String getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(String totalResults) {
            this.totalResults = totalResults;
        }

        public String getFormattedTotalResults() {
            return formattedTotalResults;
        }

        public void setFormattedTotalResults(String formattedTotalResults) {
            this.formattedTotalResults = formattedTotalResults;
        }
    }

    public static class Item {

        private String kind;
        private String title;
        private String htmlTitle;
        private String link;
        private String displayLink;
        private String snippet;
        private String htmlSnippet;
        private String cacheId;
        private String formattedUrl;
        private String htmlFormattedUrl;
        private Pagemap pagemap;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHtmlTitle() {
            return htmlTitle;
        }

        public void setHtmlTitle(String htmlTitle) {
            this.htmlTitle = htmlTitle;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDisplayLink() {
            return displayLink;
        }

        public void setDisplayLink(String displayLink) {
            this.displayLink = displayLink;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public String getHtmlSnippet() {
            return htmlSnippet;
        }

        public void setHtmlSnippet(String htmlSnippet) {
            this.htmlSnippet = htmlSnippet;
        }

        public String getCacheId() {
            return cacheId;
        }

        public void setCacheId(String cacheId) {
            this.cacheId = cacheId;
        }

        public String getFormattedUrl() {
            return formattedUrl;
        }

        public void setFormattedUrl(String formattedUrl) {
            this.formattedUrl = formattedUrl;
        }

        public String getHtmlFormattedUrl() {
            return htmlFormattedUrl;
        }

        public void setHtmlFormattedUrl(String htmlFormattedUrl) {
            this.htmlFormattedUrl = htmlFormattedUrl;
        }

        public Pagemap getPagemap() {
            return pagemap;
        }

        public void setPagemap(Pagemap pagemap) {
            this.pagemap = pagemap;
        }

        public static class Pagemap {
            private List<CseThumbnail> cse_thumbnail;
            private List<Metatags> metatags;
            private List<CseImage> cse_image;

            public List<CseThumbnail> getCse_thumbnail() {
                return cse_thumbnail;
            }

            public void setCse_thumbnail(List<CseThumbnail> cse_thumbnail) {
                this.cse_thumbnail = cse_thumbnail;
            }

            public List<Metatags> getMetatags() {
                return metatags;
            }

            public void setMetatags(List<Metatags> metatags) {
                this.metatags = metatags;
            }

            public List<CseImage> getCse_image() {
                return cse_image;
            }

            public void setCse_image(List<CseImage> cse_image) {
                this.cse_image = cse_image;
            }

            public static class CseThumbnail {

                private String width;
                private String height;
                private String src;

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }
            }

            public static class Metatags {

                private String viewport;
                @SerializedName("apple-itunes-app")
                private String appleitunesapp;
                @SerializedName("format-detection")
                private String formatdetection;
                @SerializedName("msvalidate.01")
                private String _$Msvalidate0121;

                public String getViewport() {
                    return viewport;
                }

                public void setViewport(String viewport) {
                    this.viewport = viewport;
                }

                public String getAppleitunesapp() {
                    return appleitunesapp;
                }

                public void setAppleitunesapp(String appleitunesapp) {
                    this.appleitunesapp = appleitunesapp;
                }

                public String getFormatdetection() {
                    return formatdetection;
                }

                public void setFormatdetection(String formatdetection) {
                    this.formatdetection = formatdetection;
                }

                public String get_$Msvalidate0121() {
                    return _$Msvalidate0121;
                }

                public void set_$Msvalidate0121(String _$Msvalidate0121) {
                    this._$Msvalidate0121 = _$Msvalidate0121;
                }
            }

            public static class CseImage {

                private String src;

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }
            }
        }
    }
}
