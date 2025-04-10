$(document).ready(function () {
    var lastSearch = ""; // Store the last valid search

// Trigger search on Enter key
    $("body").on("keyup", "input#parentSearchField", function (event) {
        if (event.key === "Enter") {
            lastSearch = $(this).val();
            findAll(lastSearch, 0, true);
        }
    });

// Trigger search on button click
    $("body").on("click", "button#searchButtonParent", function () {
        lastSearch = $('#parentSearchField').val();
        findAll(lastSearch, 0, true);
    });

// Pagination click event (use lastSearch)
    $("body").on("click", "li.parent-page-item", function () {
        let page = $(this).data("page");
        if (page != null) {
            console.log(page);
            findAll(lastSearch, page, true);
        }
    });

    function findAll(search, currentPage, updateUrl = false) {
        if (search.length > 400) {
            showModalMessage();
            return;
        }
        $.get({
            url: "/manager/parent-list",
            data: { search: search, currentPage: currentPage },
            success: function (responseData) {
                if (updateUrl) {
                    updateUrlParams(search, currentPage);
                }
                console.log("LOADED!");
                $("#main-content").html($(responseData).find("#main-content").html());
            },
            error: function () {
                showModalMessage();
            }
        });
    }

    function showModalMessage() {
        $("#modalMessage").text();
        $("#notificationModal").modal("show");
    }

    function updateUrlParams(search, currentPage) {
        if(search!=null && search.length>400){
        search = search.trim().substring(0, 400);
        }
        let newUrl = window.location.pathname + "?search=" + encodeURIComponent(search) + "&currentPage=" + currentPage;
        window.history.pushState({ path: newUrl }, "", newUrl);
    }
    window.onload = function() { console.log("Full page loaded!"); };

    $("body").on("click", "button#downloadButton", function () {
        $.post({
            url: "/manager/download-parent",  // URL to your download backend endpoint
            xhrFields: {
                responseType: 'blob'  // Expecting a binary response (file)
            },
            success: function (responseData) {
                // Create a link element to trigger the download
                var blob = new Blob([responseData], { type: 'text/csv' });
                var link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = "Parent-Enroll-Data.csv";  // Set the filename
                link.click();  // Trigger the download
                console.log("Download successful!");
            },
            error: function () {
                showModalMessage("Download failed!");
            }
        });
    });
});
