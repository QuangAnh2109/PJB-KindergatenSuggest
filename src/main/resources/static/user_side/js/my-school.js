
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
                        <span>${school.yourRating}</span>/5
                    </div>
                    <button class="btn-primary w-100"><a style="color: #FFFFFF" href="/public/school/details/${school.schoolId}#ratings">View Rating Details</a></button>
                </div>
            `;
        } else {
            ratingHtml = `
                <div class="my-rating">
                    <p>Your haven't rated the school yet. Please share with us your feedback</p>
                    <button class="btn-primary w-100" onclick="openRatingModal(${school.schoolId})">
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
                        <span>${school.yourRating}</span>/5
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
                        <span>${school.avgRating}</span>/5
                        (<span>${school.totalRating}</span> ratings)
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
    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            starsHtml += '<span class="star-filled">★</span>';
        } else if (i <= rating + 0.5) {
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
    document.querySelectorAll('.star-rating').forEach(ratingDiv => {
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

// Initialize star rating functionality
document.addEventListener('DOMContentLoaded', function() {
    // Set up star rating interactions
    document.querySelectorAll('.star-rating .star').forEach(star => {
        star.addEventListener('click', function() {
            const value = parseInt(this.getAttribute('data-value'));
            const ratingDiv = this.closest('.star-rating');
            const stars = ratingDiv.querySelectorAll('.star');
            const valueDisplay = ratingDiv.closest('.d-flex').querySelector('.rating-value');

            // Update data attribute
            ratingDiv.setAttribute('data-rating', value);

            // Update stars display
            stars.forEach(s => {
                const starValue = parseInt(s.getAttribute('data-value'));
                if (starValue <= value) {
                    s.className = 'fa-solid fa-star star';
                } else {
                    s.className = 'fa-regular fa-star star';
                }
            });

            // Update value display
            if (valueDisplay) {
                valueDisplay.textContent = value + '.0';
            }
        });

        // Hover effect
        star.addEventListener('mouseenter', function() {
            const hoverValue = parseInt(this.getAttribute('data-value'));
            const stars = this.closest('.star-rating').querySelectorAll('.star');

            stars.forEach(s => {
                const starValue = parseInt(s.getAttribute('data-value'));
                if (starValue <= hoverValue) {
                    s.className = 'fa-solid fa-star star';
                }
            });
        });

        star.addEventListener('mouseleave', function() {
            const ratingDiv = this.closest('.star-rating');
            const currentRating = parseInt(ratingDiv.getAttribute('data-rating'));
            const stars = ratingDiv.querySelectorAll('.star');

            stars.forEach(s => {
                const starValue = parseInt(s.getAttribute('data-value'));
                if (starValue <= currentRating) {
                    s.className = 'fa-solid fa-star star';
                } else {
                    s.className = 'fa-regular fa-star star';
                }
            });
        });
    });
});

// Submit rating data
function submitRating() {
    if (!currentSchoolId) {
        console.error('No school ID set for rating');
        return;
    }

    // Collect ratings
    const ratingItems = document.querySelectorAll('.rating-item .star-rating');
    const ratings = [];
    let valid = true;

    ratingItems.forEach((item, index) => {
        const rating = parseInt(item.getAttribute('data-rating'));
        if (rating === 0) {
            valid = false;
        }

        // Map index to rating type
        let ratingType = '';
        switch (index) {
            case 0: ratingType = 'LEARNING_PROGRAM'; break;
            case 1: ratingType = 'FACILITIES'; break;
            case 2: ratingType = 'EXTRACURRICULAR'; break;
            case 3: ratingType = 'TEACHERS_STAFF'; break;
            case 4: ratingType = 'HYGIENE_NUTRITION'; break;
        }

        ratings.push({
            type: ratingType,
            value: rating
        });
    });

    if (!valid) {
        alert('Please rate all categories before submitting');
        return;
    }

    const feedback = document.getElementById('feedback').value;

    // Create feedback data
    const feedbackData = {
        schoolId: currentSchoolId,
        feedback: feedback,
        ratings: ratings
    };

    // Submit to server
    fetch('/api/create-feedback', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(feedbackData)
    })
        .then(response => response.json())
        .then(data => {
            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('ratingModal'));
            modal.hide();

            // Reload current schools to see updated rating
            loadCurrentSchools(currentPage.currentSchools);

            // Show success message
            alert('Thank you for your feedback!');
        })
        .catch(error => {
            console.error('Error submitting rating:', error);
            alert('There was an error submitting your rating. Please try again.');
        });
}