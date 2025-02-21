package newYorkTimes

import android.webkit.WebView


fun jsStartAndObserve(
    view: WebView?,
    textColor: String,
    backgroundColor: String,
    highlightedTextColor: String
) {
    println("STARTED!!")
    val setColors = """
        ${jsClearContentFunction()}
        var textColor = '$textColor';
        var highlightedTextColor = '$highlightedTextColor';
        var backgroundColor = '$backgroundColor';
        
        function applyStyles() {
            const textElements = document.querySelectorAll('p, h1, h2, h3, h4, h5, h6, span, div, label');
            textElements.forEach((element) => {
                element.style.color = textColor;
                element.style.textDecorationColor = textColor;
            });

            const highlightedTextElements = document.querySelectorAll('a');
            highlightedTextElements.forEach((element) => {
                element.style.color = highlightedTextColor;
                element.style.textDecorationColor = highlightedTextColor;
            });
            
            document.querySelectorAll('[data-testid="live-blog-post"]').forEach((element) => {
                element.style.backgroundColor = backgroundColor;
            })
        }

        applyStyles();
        clearContent();
        
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
            
             
                    applyStyles(); 
                    clearContent();
                
            });
        });

        observer.observe(document.body, {
            childList: true,
            // for headers update
            subtree: true
        });
    """

    view?.evaluateJavascript(setColors, null)
}

fun jsSetCSSColors(
    view: WebView?,
    backgroundColor: String,
    onBackgroundColor: String
) {
    val backgroundCss = """
        body {
            background-color: $backgroundColor;
        }
        .css-eo2erm-FormBox {
            background-color: inherit;
            color: inherit;
        }
        .css-11g480x-InputBox {
            background-color: inherit;
            color: inherit;
        }
        .css-1g8ewsb-Button {
            background-color: inherit;
            color: inherit;
            border: 1px solid $onBackgroundColor;
        }
        .css-1wy7vlv {
            fill: $onBackgroundColor;
        }
    """.trimIndent()

    val setBackgroundColorJs = """
        var style = document.createElement('style');
        style.type = 'text/css';
        style.innerHTML = `$backgroundCss`;
        document.head.appendChild(style);
    """.trimIndent()

    view?.evaluateJavascript(setBackgroundColorJs, null)
}

fun jsSetPaddingForTopShadow(
    view: WebView?
) {
    val jsBody = "document.body.style.paddingTop = '10px';"
    view?.evaluateJavascript(jsBody, null)
}

fun jsUpdateJsColor(
    view: WebView?,
    backgroundColor: String,
    textColor: String,
    highlightedTextColor: String
) {
    jsSetCSSColors(view, backgroundColor, textColor)
    val jsUpdateFunctionBody = """
        backgroundColor = '$backgroundColor'
        textColor = '$textColor';
        highlightedTextColor = '$highlightedTextColor';
        applyStyles(); 
    """.trimIndent()
    view?.evaluateJavascript(jsUpdateFunctionBody, null)
}



private fun jsClearContentFunction(): String = """
        function clearContent() {
        
            const ads = document.querySelectorAll('[data-testid="StandardAd"]');
            ads.forEach((element) => {
                if (element.parent) {
                        element.parent.remove();
                        }
                })
        
            const sticky = document.querySelector('[role="navigation"]');
            if (sticky && sticky.parentElement) {
                sticky.parentElement.remove();
            }
            
            const toRemove = [
                '[data-testid="standard-dock-heading-selector"]',
                '[id="newsletter-recirculation"]',
                '[id="in-story-masthead"]',
                '[data-testid="masthead-container"]',
                '[data-testid="share-tools-menu"]',  
                '[data-testid="recirc-package"]',
                '[data-testid="recirc-rightrail"]',
                '[data-testid="site-index"]',
                '[data-testid="footer-link"]',
                '[id="bottom-wrapper"]',
                '#styln-guide',
                '[data-testid="floating-button"]',
                '[class="css-171quhb"]',
                '[data-testid="optimistic-truncator-message"]',
                           '[aria-label="The New York Times"]'
            ];

            document.querySelectorAll(toRemove)
                .forEach((element) => {
                        element.remove();
                })
                
            const allElements = document.querySelectorAll('*');
            allElements.forEach((element) => {
                const computedStyle = window.getComputedStyle(element);
                if (computedStyle.position === 'sticky') {
                    element.remove();
                }
            });
        }
    """.trimIndent()