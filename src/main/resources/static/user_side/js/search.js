
// Global variables to track the current search state
let currentPage = 0;
let currentSearchParams = {};

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Set up search button
    const searchButton = document.querySelector('.input-group .btn-primary');
    if (searchButton) {
        searchButton.addEventListener('click', performSearch);
    }

    // Set up enter key for search input
    const searchInput = document.querySelector('.input-group input[type="text"]');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }

    // Set up sort dropdown
    const sortDropdown = document.getElementById('sortBy');
    if (sortDropdown) {
        sortDropdown.addEventListener('change', function() {
            currentSearchParams.sortBy = this.value;
            performSearch();
        });
    }

    // Set up filter form buttons
    const applyFilterBtn = document.getElementById('applyFilterBtn');
    if (applyFilterBtn) {
        applyFilterBtn.addEventListener('click', applyFilters);
    }

    const clearFilterBtn = document.getElementById('clearFilterBtn');
    if (clearFilterBtn) {
        clearFilterBtn.addEventListener('click', clearFilters);
    }

    // Initialize pagination if there's data already loaded
    initializePagination();
});

// Perform search using all current parameters
function performSearch() {
    // Get basic search parameters
    const keyword = document.querySelector('.input-group input[type="text"]').value;
    const cityId = document.getElementById('citySelect').value;
    const districtId = document.getElementById('district').value;

    // Update the current search parameters
    currentSearchParams = {
        ...currentSearchParams,
        keyword: keyword,
        cityId: cityId !== "----Select City/Province-----" ? cityId : null,
        districtId: districtId !== "Please choose city/province" ? districtId : null,
        page: 0 // Reset to first page on new search
    };

    // Execute the search
    fetchSearchResults();
}

// Apply filters from the filter form
function applyFilters() {
    // Get values from filter form
    const filterForm = document.getElementById('filterForm');
    const formData = new FormData(filterForm);

    // School type
    const schoolType = formData.get('schoolType');
    currentSearchParams.typeSchool = schoolType || null;

    // Admission age
    const admissionAge = formData.get('admissionAge');
    currentSearchParams.ageRange = admissionAge || null;

    // Fees
    currentSearchParams.fee_from = formData.get('minFee') || 1;
    currentSearchParams.fee_to = formData.get('maxFee') || 20;

    // Get all selected facilities
    const facilities = formData.getAll('facilities');
    currentSearchParams.facilities = facilities.length > 0 ? facilities : null;

    // Get all selected utilities
    const utilities = formData.getAll('utilities');
    currentSearchParams.utilities = utilities.length > 0 ? utilities : null;

    // Reset page to 0 and perform search
    currentSearchParams.page = 0;
    fetchSearchResults();
}

// Clear all filters
function clearFilters() {
    const filterForm = document.getElementById('filterForm');
    filterForm.reset();

    // Reset range sliders UI
    const rangeMin = document.querySelector(".range-min");
    const rangeMax = document.querySelector(".range-max");
    const inputMin = document.querySelector(".input-min");
    const inputMax = document.querySelector(".input-max");

    rangeMin.value = 1;
    rangeMax.value = 20;
    inputMin.value = 1;
    inputMax.value = 20;

    updateProgress();

    // Remove filter parameters but keep basic search parameters
    const { keyword, cityId, districtId, sortBy } = currentSearchParams;
    currentSearchParams = {
        keyword,
        cityId,
        districtId,
        sortBy,
        page: 0,
        fee_from: 1,
        fee_to: 20
    };

    fetchSearchResults();
}

// Fetch search results from the server
function fetchSearchResults() {
    // Show loading state
    const resultsContainer = document.getElementById('search-result');
    resultsContainer.innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Loading results...</div>';

    // Build URL with parameters
    let url = '/api/search-result?';

    // Add all parameters to URL
    Object.keys(currentSearchParams).forEach(key => {
        if (currentSearchParams[key] !== null && currentSearchParams[key] !== undefined) {
            // Handle arrays (facilities, utilities)
            if (Array.isArray(currentSearchParams[key])) {
                currentSearchParams[key].forEach(value => {
                    url += `${key}=${encodeURIComponent(value)}&`;
                });
            } else {
                url += `${key}=${encodeURIComponent(currentSearchParams[key])}&`;
            }
        }
    });

    // Fetch results
    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Update results count
            updateResultsCount(data.totalElements);

            // Update search results
            displaySearchResults(data.content);

            // Update pagination
            updatePagination('searchPagination', data.totalPages, data.currentPage, loadPage);
        })
        .catch(error => {
            console.error('Error fetching search results:', error);
            resultsContainer.innerHTML = '<div class="error-message">An error occurred while fetching results. Please try again.</div>';
        });
}

// Display search results in the container
function displaySearchResults(schools) {
    const resultsContainer = document.getElementById('search-result');

    if (!schools || schools.length === 0) {
        resultsContainer.innerHTML = '<div class="no-results">No schools found matching your criteria.</div>';
        return;
    }

    let html = '';

    schools.forEach(school => {
        // Format the facilities list
        let facilitiesHtml = '';
        if (school.facilities && school.facilities.length > 0) {
            school.facilities.forEach(facility => {
                facilitiesHtml += `<span class="facility-badge">${facility}</span>`;
            });
        } else {
            facilitiesHtml = '<span>No facilities available</span>';
        }

        // Generate stars for rating
        let starsHtml = generateStarRating(school.avgRating);

        // Build the school card HTML
        html += `
        <div class="card school-card" style="overflow-y: auto; height: 350px">
            <div class="row g-0">
                <div class="col-md-4">
                    <div class="img-school">
                        <img src="${school.schoolImage}" class="img-fluid school-image" alt="School view"
                             onerror="this.onerror=null;this.src='/user_side/images/school-image/school-placeholder.png';">
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="card-body p-4">
                        <div class="d-flex mb-3" style="display: flex;justify-content: space-between;align-items: center">
                            <a href="/public/school/details/${school.schoolId}">
                                <h3 class="card-title school-title">${school.schoolName}</h3>
                            </a>
                            <div>
                                <button class="request-btn text-white" onclick="openCounselingForm()">Request Counseling</button>
                            </div>
                        </div>
                        <div class="school-info">
                            <div class="d-flex align-items-start" style="margin-bottom: 10px">
                                <div class="me-2">
                                    <i class="fas fa-map-marker-alt"></i>
                                </div>
                                <div>Address:</div>
                                <div class="text-wrap" style="word-wrap: break-word; overflow-wrap: break-word; width: 100%;">
                                    <span class="me-1"></span>
                                    <span>${school.schoolAddress}</span>
                                </div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fas fa-mail-bulk"></i>
                                </div>
                                <div>Email:</div>
                                <div class="ms-2">
                                    <a href="#" class="text-decoration-none" style="color: #1ECB15;">${school.schoolEmail}</a>
                                </div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fas fa-money-bill-wave"></i>
                                </div>
                                <div>Tuition fee:</div>
                                <div class="ms-2">From ${formatNumber(school.feeFrom)} VND/ month</div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fas fa-user-graduate"></i>
                                </div>
                                <div>Admission age:</div>
                                <div class="ms-2">From ${school.ageRange}</div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fas fa-school"></i>
                                </div>
                                <div>School type:</div>
                                <div class="ms-2">${school.schoolType}</div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fa-solid fa-star"></i>
                                </div>
                                <div>Rating:</div>
                                <div class="ms-2 star-rating">
                                    ${starsHtml}
                                    <span>${school.avgRating}</span>/5
                                    (<span>${school.totalRating}</span> ratings)
                                </div>
                            </div>
                            <div class="d-flex align-items-center mb-2">
                                <div class="info-icon text-center">
                                    <i class="fas fa-building"></i>
                                </div>
                                <div>Facilities and Utilities:</div>
                            </div>
                            <div class="facilities mt-2" style="overflow-wrap: break-word">
                                ${facilitiesHtml}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        `;
    });

    resultsContainer.innerHTML = html;
}

// Generate star rating HTML
function generateStarRating(rating) {
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

// Update the results count message
function updateResultsCount(totalElements) {
    const messagesDiv = document.getElementById('messages');
    if (messagesDiv) {
        const messageSpan = messagesDiv.querySelector('.result-message span:nth-child(2)');
        if (messageSpan) {
            messageSpan.textContent = totalElements;
        }
    }
}

// Initialize pagination on page load
function initializePagination() {
    // Check if there's already pagination in the HTML
    const paginationContainer = document.querySelector('.pagination');
    if (paginationContainer) {
        // Replace the static pagination with an element we can control
        const paginationParent = paginationContainer.parentElement;
        paginationParent.innerHTML = '<ul id="searchPagination" class="pagination"></ul>';
    }
}

// Load a specific page of results
function loadPage(page) {
    currentSearchParams.page = page;
    fetchSearchResults();

    // Scroll to top of results
    document.getElementById('search-result').scrollIntoView({ behavior: 'smooth' });
}

// Open the counseling form modal
function openCounselingForm() {
    const counselingModal = new bootstrap.Modal(document.getElementById('counselingModal'));
    counselingModal.show();
}

// Helper function to update slider progress
function updateProgress() {
    const rangeMin = document.querySelector(".range-min");
    const rangeMax = document.querySelector(".range-max");
    const progress = document.querySelector(".progress");

    if (rangeMin && rangeMax && progress) {
        let minVal = parseInt(rangeMin.value);
        let maxVal = parseInt(rangeMax.value);
        let rangeMinValue = parseInt(rangeMin.min);
        let rangeMaxValue = parseInt(rangeMax.max);
        let range = rangeMaxValue - rangeMinValue;

        progress.style.left = ((minVal - rangeMinValue) / range) * 100 + "%";
        progress.style.right = 100 - ((maxVal - rangeMinValue) / range) * 100 + "%";
    }
}