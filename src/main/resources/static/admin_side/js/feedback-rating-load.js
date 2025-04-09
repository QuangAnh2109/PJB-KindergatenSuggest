function getRatingByTime(schoolId) {
    let fromDateElement = document.getElementById("fromDate");
    let toDateElement = document.getElementById("toDate");

    let fromDate = fromDateElement && fromDateElement.value ? new Date(fromDateElement.value).toISOString() : null;
    let toDate = toDateElement && toDateElement.value ? new Date(toDateElement.value).toISOString() : null;

    $.ajax({
        url: "/manager/school/rating/search/",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
            schoolId: schoolId,
            fromDate: fromDate,
            toDate: toDate
        }),
        success: function(json) {
            document.getElementById("main-contain").innerHTML = json;
        },
        error: function(xhr) {
            console.log(xhr.responseText);
            $('#unenrollModel').modal('show');
        }
    });
}

function searchFeedback(schoolId, pageNumber, callback) {
    let fromDateElement = document.getElementById("fromDate");
    let toDateElement = document.getElementById("toDate");

    let fromDate = fromDateElement && fromDateElement.value ? new Date(fromDateElement.value).toISOString() : null;
    let toDate = toDateElement && toDateElement.value ? new Date(toDateElement.value).toISOString() : null;

    $.ajax({
        url: "/manager/school/feedback/search/",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
            schoolId: schoolId,
            pageNumber: pageNumber,
            fromDate: fromDate,
            toDate: toDate,
            rating: getSelectedRatings()
        }),
        success: function (html) {
            callback(html);
        },
        error: function (xhr) {
            console.log(xhr.responseText);
            $('#unenrollModel').modal('show');
        },
    });
}

function loadMoreFeedbackList(schoolId, pageNumber) {
    searchFeedback(schoolId, pageNumber, function(html) {
        document.getElementById("loadMoreFeedback-button").outerHTML = html;
    });
}

function reloadFeedBackList(schoolId){
    searchFeedback(schoolId, 0, function(html) {
        document.getElementById("feedback-list").innerHTML = html;
    });
}

function getSelectedRatings() {
    const selectedRatings = [];

    for (let i = 1; i <= 5; i++) {
        const checkbox = document.getElementById(`search-star-${i}`);

        if (checkbox && checkbox.checked) {
            selectedRatings.push(i);
        }
    }
    if(selectedRatings.length === 0) return null
    return selectedRatings;
}