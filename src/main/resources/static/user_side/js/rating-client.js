// Initialize Bootstrap modal
let ratingModal;
let toastContainer;

document.addEventListener('DOMContentLoaded', function () {
    ratingModal = new bootstrap.Modal(document.getElementById('ratingModal'));

    // Create toast container if it doesn't exist
    if (!document.getElementById('toastContainer')) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    } else {
        toastContainer = document.getElementById('toastContainer');
    }

    // Initialize star rating functionality
    document.querySelectorAll('.star-rating').forEach(function (ratingContainer) {
        const stars = ratingContainer.querySelectorAll('.star');

        stars.forEach(function (star) {
            star.addEventListener('click', function (e) {
                const starValue = parseInt(this.getAttribute('data-value'));
                const starRect = star.getBoundingClientRect();
                const clickX = e.clientX - starRect.left;
                const starWidth = starRect.width;

                // Determine if click is on the left half (half star) or right half (full star)
                let rating;
                if (clickX < starWidth / 2) {
                    rating = starValue - 0.5;
                } else {
                    rating = starValue;
                }

                updateStars(ratingContainer, rating);
            });

            // Mouseover behavior to preview ratings
            star.addEventListener('mousemove', function(e) {
                const starValue = parseInt(this.getAttribute('data-value'));
                const starRect = star.getBoundingClientRect();
                const mouseX = e.clientX - starRect.left;
                const starWidth = starRect.width;

                let rating;
                if (mouseX < starWidth / 2) {
                    rating = starValue - 0.5;
                } else {
                    rating = starValue;
                }

                highlightStars(ratingContainer, rating);
            });
        });

        // Reset to selected rating when mouse leaves
        ratingContainer.addEventListener('mouseleave', function() {
            const currentRating = parseFloat(ratingContainer.getAttribute('data-rating') || 0);
            highlightStars(ratingContainer, currentRating);
        });
    });
});

// Function to show toast notification
function showToast(message, type = 'success') {
    const toastId = 'toast-' + Date.now();
    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type}" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, { delay: 5000 });
    toast.show();

    // Remove the toast element after it's hidden
    toastElement.addEventListener('hidden.bs.toast', function() {
        toastElement.remove();
    });
}

// Function to open the rating modal with a specific schoolId
function openRatingModal() {
    const modalElement = document.getElementById('ratingModal');
    if (modalElement) {
        // Get the school ID from the modal's data attribute (set in the HTML)
        const schoolId = modalElement.getAttribute('data-school-id');
        if (!schoolId) {
            // If not set yet, get from URL or another source
            const urlParams = new URLSearchParams(window.location.search);
            const schoolIdFromUrl = urlParams.get('schoolId');

            if (schoolIdFromUrl) {
                modalElement.setAttribute('data-school-id', schoolIdFromUrl);
            }
        }

        // Reset all ratings to 0
        document.querySelectorAll('.star-rating').forEach(function(container) {
            updateStars(container, 0);
        });

        // Clear feedback textarea
        const feedbackElement = document.getElementById('feedback');
        if (feedbackElement) {
            feedbackElement.value = '';
        }

        // Open the modal
        ratingModal.show();
    } else {
        console.error('Rating modal element not found');
    }
}

function updateStars(container, value) {
    container.setAttribute('data-rating', value);
    highlightStars(container, value);

    // Update rating value display
    const parent = container.closest('.d-flex');
    if (parent) {
        const ratingValue = parent.querySelector('.rating-value');
        if (ratingValue) {
            ratingValue.textContent = value.toFixed(1);
        }
    }
}

function highlightStars(container, value) {
    const fullStars = Math.floor(value);
    const hasHalf = value % 1 >= 0.5;

    container.querySelectorAll('.star').forEach(function (star) {
        const starValue = parseInt(star.getAttribute('data-value'));

        if (starValue <= fullStars) {
            // Full star
            star.className = 'fa-solid fa-star star active';
        } else if (hasHalf && starValue === fullStars + 1) {
            // Half star
            star.className = 'fa-solid fa-star-half-stroke star active';
        } else {
            // Empty star
            star.className = 'fa-regular fa-star star';
        }
    });

    // Update rating value display
    const parent = container.closest('.d-flex');
    if (parent) {
        const ratingValue = parent.querySelector('.rating-value');
        if (ratingValue) {
            ratingValue.textContent = value.toFixed(1);
        }
    }
}

function submitRating() {
    const modalElement = document.getElementById('ratingModal');
    if (!modalElement) {
        showToast('Modal element not found', 'danger');
        return;
    }

    const schoolId = modalElement.getAttribute('data-school-id');
    const feedback = document.getElementById('feedback').value.trim();

    // Get all ratings and ensure they're parsed as floats, not integers
    const learningProgram = parseFloat(document.querySelector('.rating-item:nth-child(1) .star-rating').getAttribute('data-rating') || 0);
    const facilities = parseFloat(document.querySelector('.rating-item:nth-child(2) .star-rating').getAttribute('data-rating') || 0);
    const extracurricular = parseFloat(document.querySelector('.rating-item:nth-child(3) .star-rating').getAttribute('data-rating') || 0);
    const teachers = parseFloat(document.querySelector('.rating-item:nth-child(4) .star-rating').getAttribute('data-rating') || 0);
    const hygiene = parseFloat(document.querySelector('.rating-item:nth-child(5) .star-rating').getAttribute('data-rating') || 0);

    // Create the feedback data object matching the backend FeedbackVo structure
    const data = {
        schoolId: parseInt(schoolId),
        learningProgram: learningProgram,
        facilitiesUtilities: facilities,
        extracurricularActivities: extracurricular,
        teacherStaff: teachers,
        hygieneNutrition: hygiene,
        feedbackMessage: feedback
    };

    // Validate locally before sending to the server
    if (learningProgram === 0 || facilities === 0 || extracurricular === 0 ||
        teachers === 0 || hygiene === 0) {
        showToast("Please provide ratings for all categories.", "danger");
        return;
    }

    // Submit to the backend
    fetch('/api/create-feedback', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.error || JSON.stringify(errorData));
                });
            }
            return response.text();
        })
        .then(data => {
            showToast("Thank you for your rating and feedback!", "success");
            ratingModal.hide();
            // Reload the page to show the updated ratings after a short delay
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        })
        .catch(error => {
            console.error('Error:', error);
            showToast("Error: " + error.message, "danger");
        });
}