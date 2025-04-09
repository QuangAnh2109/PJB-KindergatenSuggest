// Initialize Bootstrap modal
let ratingModal;
let toastContainer;

function showToast(message, type) {
    // Generate a unique ID for this toast
    const toastId = `toast-${Date.now()}`;

    // Map type to Bootstrap color classes and icons
    const typeClasses = {
        'success': 'bg-success text-white',
        'danger': 'bg-danger text-white',
        'warning': 'bg-warning text-dark',
        'info': 'bg-info text-dark'
    };

    const icons = {
        'success': '<i class="fas fa-check-circle me-2"></i>',
        'danger': '<i class="fas fa-exclamation-circle me-2"></i>',
        'warning': '<i class="fas fa-exclamation-triangle me-2"></i>',
        'info': '<i class="fas fa-info-circle me-2"></i>'
    };

    // Create toast HTML
    const toast = document.createElement('div');
    toast.id = toastId;
    toast.className = `toast ${typeClasses[type] || 'bg-light'} border-0`;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');

    // Set custom styles for increased width and proper wrapping
    toast.style.maxWidth = '400px';
    toast.style.width = 'auto';

    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body" style="word-wrap: break-word; word-break: break-word; flex: 1;">
                ${icons[type] || ''}${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto"
                    data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;

    // Append toast to container
    const toastContainer = document.querySelector('.toast-container');
    if (toastContainer) {
        toastContainer.appendChild(toast);
    } else {
        // Create container if it doesn't exist
        const newContainer = document.createElement('div');
        newContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        newContainer.style.zIndex = '1050';
        document.body.appendChild(newContainer);
        newContainer.appendChild(toast);
    }

    // Initialize Bootstrap toast and show it
    const bsToast = new bootstrap.Toast(toast, {
        autohide: true,
        delay: 5000
    });

    bsToast.show();

    // Remove toast element after it's hidden
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}


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
        showToast("Modal element not found", "danger");
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

            // Extract the error message
            let errorMessage = "An unexpected error occurred";

            try {
                // Try to parse the error message
                if (error.message) {
                    // Check if the error message is a JSON string
                    if (error.message.startsWith('{') && error.message.endsWith('}')) {
                        const errorObj = JSON.parse(error.message);

                        // Build a formatted error message from the object
                        if (typeof errorObj === 'object') {
                            errorMessage = '';
                            for (const key in errorObj) {
                                if (errorObj.hasOwnProperty(key)) {
                                    errorMessage += `• ${errorObj[key]}<br>`;
                                }
                            }
                        } else {
                            errorMessage = error.message;
                        }
                    } else {
                        // Not JSON, use the message directly
                        errorMessage = error.message;
                    }
                }
            } catch (e) {
                // If parsing fails, use the original error message
                console.error('Error parsing error message:', e);
                errorMessage = error.message || errorMessage;
            }

            showToast(errorMessage, "danger");
        });
}


