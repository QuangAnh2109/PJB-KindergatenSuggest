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
            showErrorMessage(xhr);
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
            showErrorMessage(xhr);
        },
    });
}

function showErrorMessage(message) {
    if(xhr.status === 422){
        if(xhr.responseJSON.dateFrom != null){
            var fromDateError = document.getElementById("fromDate-error");
            fromDateError.textContent = xhr.responseJSON.dateFrom;
            fromDateError.style.display = 'block';
        }
        if(xhr.responseJSON.dateTo != null){
            var toDateError = document.getElementById("toDate-error");
            toDateError.textContent = xhr.responseJSON.dateTo;
            toDateError.style.display = 'block';
        }
        if(xhr.responseJSON.date != null){
            var dateError = document.getElementById("date-error");
            dateError.textContent = xhr.responseJSON.date;
            dateError.style.display = 'block';
        }
    }
    else{
        document.getElementById("msg-popup-1").textContent = xhr.responseJSON.message;

        var modalElement = document.getElementById('unenrollModel');

        var modal = new bootstrap.Modal(modalElement);

        modal.show('unenrollModel');
    }
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