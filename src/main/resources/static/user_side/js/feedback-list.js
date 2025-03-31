document.addEventListener('DOMContentLoaded', function() {
    // Get all filter buttons
    const filterButtons = document.querySelectorAll('.filter-btn');

    // Get the school ID from the page URL
    const pathParts = window.location.pathname.split('/');
    const schoolId = pathParts[pathParts.length - 1];

    // Call API to load all feedback initially (filter=0 means all)
    fetchFilteredFeedback(schoolId, "0");

    // Add click event listeners to all filter buttons
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Remove active class from all buttons
            filterButtons.forEach(btn => btn.classList.remove('active'));

            // Add active class to clicked button
            this.classList.add('active');

            // Get filter value
            let filterValue = this.textContent.trim().split(' ')[0];

            // If it's "All", set it to 0 (represents all ratings)
            if (filterValue === "All") {
                filterValue = "0";
            }

            // Call API to get filtered feedback
            fetchFilteredFeedback(schoolId, filterValue);
        });
    });
});

function fetchFilteredFeedback(schoolId, filter) {
    // Show loading indicator
    const feedbackContainer = document.getElementById('review-item-list');
    feedbackContainer.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>';

    // Call API
    fetch(`/api/filter-feedback?schoolId=${schoolId}&filter=${filter}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server returned ${response.status}: ${response.statusText}`);
            }

            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                throw new Error(`Expected JSON response but got ${contentType || 'unknown'} content type`);
            }

            return response.json();
        })
        .then(data => {
            updateFeedbackList(data || [], feedbackContainer);
        })
        .catch(error => {
            console.error('Error fetching filtered feedback:', error);
            feedbackContainer.innerHTML = `<div class="alert alert-danger">
                <p>Error loading feedback: ${error.message}</p>
                <p>Please try again later or contact support if the problem persists.</p>
            </div>`;
        });
}

function updateFeedbackList(feedbackList, container) {
    // Clear the container
    container.innerHTML = '';

    // Handle empty or null feedbackList
    if (!feedbackList || !Array.isArray(feedbackList) || feedbackList.length === 0) {
        container.innerHTML = `
            <div class="alert alert-info text-center">
                <i class="fa-solid fa-circle-info"></i>
                No feedback found for this rating filter.
            </div>`;
        return;
    }

    // Add each feedback item to the container
    feedbackList.forEach(review => {
        const reviewHtml = createFeedbackHtml(review);
        container.innerHTML += reviewHtml;
    });
}

function createFeedbackHtml(review) {
    // Format date from UTC to readable format
    const feedbackDate = new Date(review.feedbackTime).toLocaleString();

    // Create star HTML for average rating
    const avgStarsHtml = createStarRatingHtml(review.avgRating, 25);

    // Create star HTML for individual categories
    const learningStarsHtml = createStarRatingHtml(review.learningProgram, 20);
    const facilitiesStarsHtml = createStarRatingHtml(review.facilitiesUtilities, 20);
    const extracurricularStarsHtml = createStarRatingHtml(review.extracurricularActivities, 20);
    const teachersStarsHtml = createStarRatingHtml(review.teacherStaff, 20);
    const hygieneStarsHtml = createStarRatingHtml(review.hygieneNutrition, 20);

    // Create and return the review HTML
    return `
        <div class="border p-3 rounded review-item">
            <div class="d-flex align-items-center">
                <img src="${review.accountImage || '/user_side/images/profile/User_Placeholder.png'}" 
                     class="rounded-circle me-2 img-user" 
                     alt="User" 
                     onerror="this.onerror=null;this.src='/user_side/images/profile/User_Placeholder.png'">
                <div>
                    <strong>${review.accountName}</strong>
                    <div class="text-muted">${feedbackDate}</div>
                </div>
            </div>
            <p class="mt-2">${review.feedback}</p>
            <div class="d-flex align-items-center">
                <div class="rating-stars me-2">${avgStarsHtml}</div>
                <span>${review.avgRating}/5</span>
            </div>
            <div class="mt-3">
                <div class="d-flex justify-content-between">
                    <span>Learning program:</span>
                    <span>${learningStarsHtml} (${review.learningProgram}/5)</span>
                </div>
                <div class="d-flex justify-content-between">
                    <span>Facilities and Utilities:</span>
                    <span>${facilitiesStarsHtml} (${review.facilitiesUtilities}/5)</span>
                </div>
                <div class="d-flex justify-content-between">
                    <span>Extracurricular Activities:</span>
                    <span>${extracurricularStarsHtml} (${review.extracurricularActivities}/5)</span>
                </div>
                <div class="d-flex justify-content-between">
                    <span>Teachers and Staff:</span>
                    <span>${teachersStarsHtml} (${review.teacherStaff}/5)</span>
                </div>
                <div class="d-flex justify-content-between">
                    <span>Hygiene and Nutrition:</span>
                    <span>${hygieneStarsHtml} (${review.hygieneNutrition}/5)</span>
                </div>
            </div>
        </div>
    `;
}

function createStarRatingHtml(rating, fontSize) {
    let html = '';
    for (let i = 1; i <= 5; i++) {
        let starClass = 'star-empty';
        if (i <= rating) {
            starClass = 'star-filled';
        } else if (i <= rating + 0.5) {
            starClass = 'star-half';
        }
        html += `<span style="font-size: ${fontSize}px" class="${starClass}">★</span>`;
    }
    return html;
}


