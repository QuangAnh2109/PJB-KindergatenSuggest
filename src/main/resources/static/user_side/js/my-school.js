
// Show and hide loading animation
function showLoading(containerId) {
    const container = document.getElementById(containerId);

    // Create loader if it doesn't exist
    if (!container.querySelector('.loader-container')) {
        const loaderContainer = document.createElement('div');
        loaderContainer.className = 'loader-container';
        loaderContainer.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2">Loading schools...</p>
        `;

        // Clear existing content temporarily
        const existingContent = container.querySelector('.row');
        if (existingContent) {
            existingContent.style.display = 'none';
        }

        // Add loader before pagination
        container.insertBefore(loaderContainer, container.querySelector('nav'));
    }
}

function hideLoading(containerId) {
    const container = document.getElementById(containerId);
    const loader = container.querySelector('.loader-container');

    if (loader) {
        loader.remove();

        // Restore content visibility
        const existingContent = container.querySelector('.row');
        if (existingContent) {
            existingContent.style.display = '';
        }
    }
}


// Global variables to track pagination state
let currentPage = {
    currentSchools: 0,
    previousSchools: 0
};

// Initialize the page when document is ready
document.addEventListener('DOMContentLoaded', function() {
    // Set up initial pagination
    loadCurrentSchools(0);

    // Set up tab change listeners
    document.querySelectorAll('#schoolTabs .nav-link').forEach(tab => {
        tab.addEventListener('click', function(e) {
            const tabId = e.target.getAttribute('href').substring(1);
            if (tabId === 'currentSchools' && currentPage.currentSchools === 0) {
                loadCurrentSchools(0);
            } else if (tabId === 'previousSchools' && currentPage.previousSchools === 0) {
                loadPreviousSchools(0);
            }
        });
    });
});

// Function to load current schools
function loadCurrentSchools(page) {
    showLoading('currentSchools');

    fetch(`/api/api/current-schools?page=${page}`)
        .then(response => response.json())
        .then(data => {
            currentPage.currentSchools = data.currentPage;
            hideLoading('currentSchools');
            displayCurrentSchools(data);
            updatePagination('current-school-tab', data.totalPages, data.currentPage, loadCurrentSchools);
        })
        .catch(error => {
            console.error('Error loading current schools:', error);
            hideLoading('currentSchools');
            // Show error message
            const container = document.getElementById('currentSchools');
            const content = container.querySelector('.row') || document.createElement('div');
            content.className = 'row';
            content.innerHTML = '<div class="alert alert-danger">Failed to load schools. Please try again later.</div>';
            container.insertBefore(content, container.querySelector('nav'));
        });
}

// Function to load previous schools
function loadPreviousSchools(page) {
    showLoading('previousSchools');

    fetch(`/api/api/previous-schools?page=${page}`)
        .then(response => response.json())
        .then(data => {
            currentPage.previousSchools = data.currentPage;
            hideLoading('previousSchools');
            displayPreviousSchools(data);
            updatePagination('previous-school-tab', data.totalPages, data.currentPage, loadPreviousSchools);
        })
        .catch(error => {
            console.error('Error loading previous schools:', error);
            hideLoading('previousSchools');
            // Show error message
            const container = document.getElementById('previousSchools');
            const content = container.querySelector('.row') || document.createElement('div');
            content.className = 'row';
            content.innerHTML = '<div class="alert alert-danger">Failed to load schools. Please try again later.</div>';
            container.insertBefore(content, container.querySelector('nav'));
        });
}
// Display current schools in the UI
function displayCurrentSchools(data) {
    const container = document.getElementById('currentSchools');
    const content = container.querySelector('.row') || document.createElement('div');
    content.className = 'row';

    if (data.totalElements === 0) {
        content.innerHTML = `
            <div class="row school align-content-center justify-content-center" style="height: 50px">
                <div>You are not currently enrolled in any school !</div>
            </div>
        `;
    } else {
        // Clear existing content
        content.innerHTML = '';

        // Loop through schools and create HTML for each
        data.content.forEach(school => {
            const schoolElement = document.createElement('div');
            schoolElement.className = 'row school';
            schoolElement.innerHTML = createSchoolHtml(school, true);
            content.appendChild(schoolElement);
        });
    }

    // Replace existing content or append new content
    const existingContent = container.querySelector('.row');
    if (existingContent) {
        container.replaceChild(content, existingContent);
    } else {
        container.insertBefore(content, container.querySelector('nav'));
    }
}

// Display previous schools in the UI
function displayPreviousSchools(data) {
    const container = document.getElementById('previousSchools');
    const content = container.querySelector('.row') || document.createElement('div');
    content.className = 'row';

    if (data.totalElements === 0) {
        content.innerHTML = `
            <div class="row school align-content-center justify-content-center" style="height: 50px">
                <div>You have never been admitted to any school in the system !</div>
            </div>
        `;
    } else {
        // Clear existing content
        content.innerHTML = '';

        // Loop through schools and create HTML for each
        data.content.forEach(school => {
            const schoolElement = document.createElement('div');
            schoolElement.className = 'row school';
            schoolElement.innerHTML = createSchoolHtml(school, false);
            content.appendChild(schoolElement);
        });
    }

    // Replace existing content or append new content
    const existingContent = container.querySelector('.row');
    if (existingContent) {
        container.replaceChild(content, existingContent);
    } else {
        container.insertBefore(content, container.querySelector('nav'));
    }
}

// Create HTML for a school card
function createSchoolHtml(school, isCurrentSchool) {
    // Generate star rating
    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= school.avgRating) {
            starsHtml += '<span class="star-filled">★</span>';
        } else if (i <= school.avgRating + 0.5) {
            starsHtml += '<span class="star-half">★</span>';
        } else {
            starsHtml += '<span class="star-empty">★</span>';
        }
    }

    // Format facilities
    let facilitiesHtml = '';
    if (school.facilities && school.facilities.length > 0) {
        facilitiesHtml = school.facilities.map(facility =>
            `<span class="facility-badge">${facility}</span>`
        ).join('');
    } else {
        facilitiesHtml = '<span class="facility-badge">No facilities available</span>';
    }


    // Create the rating section based on whether the user has rated the school
    let ratingHtml = '';
    if (isCurrentSchool) {
        if (school.yourRating !== 0) {
            ratingHtml = `
                <div class="my-rating">
                    <p>Your Average Rating</p>
                    <div class="star-rating">
                        ${generateUserRatingStars(school.yourRating)}
                        <span>${roundRating(school.yourRating)}</span>/5
                    </div>
                    <button class="btn-primary w-100"><a style="color: #FFFFFF" href="/public/school/details/${school.schoolId}#ratings">View Rating Details</a></button>
                </div>
            `;
        } else {
            ratingHtml = `
                <div class="my-rating">
                    <p>Your haven't rated the school yet. Please share with us your feedback</p>
                    <button class="btn-primary w-100" style="color: #FFFFFF" onclick="openRatingModal(${school.schoolId})">
                        Rate School
                    </button>
                </div>
            `;
        }
    } else {
        if (school.yourRating !== 0) {
            ratingHtml = `
                <div class="my-rating">
                    <p>Your Average Rating</p>
                    <div class="star-rating">
                        ${generateUserRatingStars(school.yourRating)}
                        <span>${roundRating(school.yourRating)}</span>/5
                    </div>
                    <button class="btn-primary w-100"><a style="color: #FFFFFF" href="/public/school/details/${school.schoolId}#ratings">View Rating Details</a></button>                
                    </div>
            `;
        } else {
            ratingHtml = `
                <div class="my-rating">
                    <p style="color: #1ECB15"><b>There's no rating of yours for this school. You can only rate the school you're currently enrolled in</b></p>
                </div>
            `;
        }
    }

    return `
        <div class="col-md-9">
            <div class="school-card d-flex">
                <div class="img-school col-md-2 justify-content-center align-content-center">
                    <img src="${school.schoolImage}" class="me-3" alt="school-image" onerror="this.onerror=null;this.src='/user_side/images/school-image/school-placeholder.png';">
                    <div class="star-rating">
                        ${starsHtml}
                        <span>${roundRating(school.avgRating)}</span>/5
                        (<span>${school.totalRating} </span> ratings)
                    </div>
                </div>
                <div class="school-detail">
                    <h4><a href="/public/school/details/${school.schoolId}" class="text-primary fw-bold"> <span>${school.schoolName}</span></a></h4>
                    <ul>
                        <li><strong>Address:</strong> <span>${school.schoolAddress}</span></li>
                        <li><strong>Email:</strong> <a href="mailto:${school.schoolEmail}"><span>${school.schoolEmail}</span></a></li>
                        <li><strong>Tuition fee:</strong> From <span>${formatNumber(school.feeFrom)}</span> VND/month</li>
                        <li><strong>Admission age:</strong> From <span>${school.ageRange}</span></li>
                        <li><strong>School type:</strong> <span>${school.schoolType}</span></li>
                        <li><strong>Enrolled Date:</strong> <span>${school.enrollDate}</span></li>
                        ${isCurrentSchool ? '' : `<li><strong>Enrolled End Date:</strong> <span>${school.enrollEndDate}</span></li>`}
                        <li class="facilities" style="overflow-wrap: break-word">
                            <strong>Facilities and Utilities:</strong>
                            ${facilitiesHtml}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            ${ratingHtml}
        </div>
    `;
}

// Generate star rating HTML for user rating
function generateUserRatingStars(rating) {
    // Round up to nearest 0.5
    rating = Math.ceil(rating * 2) / 2;

    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            starsHtml += '<span class="star-filled">★</span>';
        } else if (i - 0.5 === rating) {
            starsHtml += '<span class="star-half">★</span>';
        } else {
            starsHtml += '<span class="star-empty">★</span>';
        }
    }
    return starsHtml;
}


// Format number with commas
function formatNumber(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// Update pagination UI
function updatePagination(paginationId, totalPages, currentPage, loadFunction) {
    const paginationContainer = document.getElementById(paginationId);
    if (!paginationContainer) return;

    // Clear existing pagination
    paginationContainer.innerHTML = '';

    // Previous button
    const prevLi = document.createElement('li');
    prevLi.className = 'page-item' + (currentPage === 0 ? ' disabled' : '');
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = 'javascript:void(0)';
    prevLink.textContent = 'Previous';
    if (currentPage > 0) {
        prevLink.addEventListener('click', () => loadFunction(currentPage - 1));
    }
    prevLi.appendChild(prevLink);
    paginationContainer.appendChild(prevLi);

    // Page numbers
    const maxPageButtons = 5; // Maximum number of page buttons to show
    let startPage = Math.max(0, currentPage - Math.floor(maxPageButtons / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPageButtons - 1);

    if (endPage - startPage + 1 < maxPageButtons && startPage > 0) {
        startPage = Math.max(0, endPage - maxPageButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = 'page-item' + (i === currentPage ? ' active' : '');
        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = 'javascript:void(0)';
        pageLink.textContent = (i + 1).toString();
        pageLink.addEventListener('click', () => loadFunction(i));
        pageLi.appendChild(pageLink);
        paginationContainer.appendChild(pageLi);
    }

    // Next button
    const nextLi = document.createElement('li');
    nextLi.className = 'page-item' + (currentPage >= totalPages - 1 ? ' disabled' : '');
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = 'javascript:void(0)';
    nextLink.textContent = 'Next';
    if (currentPage < totalPages - 1) {
        nextLink.addEventListener('click', () => loadFunction(currentPage + 1));
    }
    nextLi.appendChild(nextLink);
    paginationContainer.appendChild(nextLi);
}

// For the rating modal functionality
let currentSchoolId = null;

function openRatingModal(schoolId) {
    currentSchoolId = schoolId;

    // Reset all star ratings
    document.querySelectorAll('.rating-item .star-rating').forEach(ratingDiv => {
        ratingDiv.setAttribute('data-rating', '0');

        ratingDiv.querySelectorAll('.star').forEach(star => {
            star.className = 'fa-regular fa-star star';
        });

        const valueDisplay = ratingDiv.closest('.d-flex').querySelector('.rating-value');
        if (valueDisplay) {
            valueDisplay.textContent = '0.0';
        }
    });

    // Clear feedback text
    document.getElementById('feedback').value = '';

    // Show the modal
    const ratingModal = new bootstrap.Modal(document.getElementById('ratingModal'));
    ratingModal.show();
}

// Highlight stars based on rating value
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
}

// Update stars and rating value
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

// Initialize star rating functionality
document.addEventListener('DOMContentLoaded', function() {
    // Set up star rating interactions
    document.querySelectorAll('.star-rating .star').forEach(star => {
        // Handle click event for setting the rating
        star.addEventListener('click', function(e) {
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

            const ratingDiv = this.closest('.star-rating');
            updateStars(ratingDiv, rating);
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

            const ratingDiv = this.closest('.star-rating');
            highlightStars(ratingDiv, rating);
        });

        // Reset to selected rating when mouse leaves
        star.closest('.star-rating').addEventListener('mouseleave', function() {
            const currentRating = parseFloat(this.getAttribute('data-rating') || 0);
            highlightStars(this, currentRating);
        });
    });
});

function submitRating() {
    if (!currentSchoolId) {
        showToast("No school selected for rating", "danger");
        return;
    }

    // Collect ratings
    const learningProgram = parseFloat(document.querySelector('.rating-item:nth-child(1) .star-rating').getAttribute('data-rating') || 0);
    const facilities = parseFloat(document.querySelector('.rating-item:nth-child(2) .star-rating').getAttribute('data-rating') || 0);
    const extracurricular = parseFloat(document.querySelector('.rating-item:nth-child(3) .star-rating').getAttribute('data-rating') || 0);
    const teachers = parseFloat(document.querySelector('.rating-item:nth-child(4) .star-rating').getAttribute('data-rating') || 0);
    const hygiene = parseFloat(document.querySelector('.rating-item:nth-child(5) .star-rating').getAttribute('data-rating') || 0);
    const feedback = document.getElementById('feedback').value.trim();

    // Create the feedback data object matching the backend FeedbackVo structure
    const data = {
        schoolId: parseInt(currentSchoolId),
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

    // Client-side validation for feedback message
    if (!feedback || feedback.length < 50) {
        showToast("Feedback message must be at least 50 characters", "danger");
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
                // For error responses, try to parse as JSON first
                return response.text().then(text => {
                    try {
                        // Try to parse as JSON
                        const errorData = JSON.parse(text);
                        if (errorData.feedbackMessage) {
                            throw new Error(errorData.feedbackMessage);
                        } else if (errorData.message) {
                            throw new Error(errorData.message);
                        } else if (typeof errorData === 'string') {
                            throw new Error(errorData);
                        } else {
                            throw new Error(`Error ${response.status}: ${response.statusText}`);
                        }
                    } catch (e) {
                        // If parsing fails, it's a plain text error
                        if (e instanceof SyntaxError) {
                            throw new Error(text || `Error ${response.status}: ${response.statusText}`);
                        }
                        throw e;
                    }
                });
            }

            // For successful responses, try to parse as JSON, but fall back to text
            return response.text().then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    // If it's not JSON, return the text as is
                    return text;
                }
            });
        })
        .then(data => {
            // Handle successful response (could be object or string)
            let successMessage;
            if (typeof data === 'object' && data.message) {
                successMessage = data.message;
            } else if (typeof data === 'string') {
                successMessage = data;
            } else {
                successMessage = "Thank you for your rating and feedback!";
            }

            showToast(successMessage, "success");

            // Close modal - make sure it exists and fetch current instance
            const modalElement = document.getElementById('ratingModal');
            if (modalElement) {
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                if (modalInstance) {
                    modalInstance.hide();
                } else {
                    // If for some reason the instance isn't found, try creating a new one
                    const newModal = new bootstrap.Modal(modalElement);
                    newModal.hide();
                }
            }

            // Reset form values for future use
            document.querySelectorAll('.star-rating').forEach(rating => {
                updateStars(rating, 0);
            });
            document.getElementById('feedback').value = '';

            // Reload current schools to see updated rating after short delay
            setTimeout(() => {
                loadCurrentSchools(currentPage.currentSchools);
            }, 1000);
        })
        .catch(error => {
            console.error('Error submitting feedback:', error);
            showToast(error.message || "Failed to submit your feedback. Please try again later.", "danger");
        });
}

// Function to show toast notification
function showToast(message, type = 'success') {
    // Check if toastContainer exists, create it if not
    let toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }

    const toastId = 'toast-' + Date.now();
    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type}" role="alert" aria-live="assertive" aria-atomic="true" style="min-width: 300px; max-width: 400px;">
            <div class="d-flex">
                <div class="toast-body" style="white-space: normal; word-wrap: break-word;">
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
function roundRating(rating) {
    return Math.ceil(rating * 2) / 2;
}