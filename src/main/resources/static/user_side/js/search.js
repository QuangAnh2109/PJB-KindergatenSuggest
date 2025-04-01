function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// Get all filter values (no caching to ensure fresh values)
function getFilterValues() {
    const keyword = document.querySelector('input[placeholder="Enter a school name"]').value || '';
    const cityId = document.querySelector('#citySelect').value || '0';
    const districtId = document.querySelector('#district').value || '0';
    const schoolType = document.querySelector('select[name="schoolType"]').value || '';
    const admissionAge = document.querySelector('select[name="admissionAge"]').value || '';
    const minFee = document.querySelector('input[name="minFee"]').value || '';
    const maxFee = document.querySelector('input[name="maxFee"]').value || '';

    // Get checkbox values directly
    const facilities = Array.from(
        document.querySelectorAll('input[name="facilities"]:checked'),
        checkbox => checkbox.value
    );

    const utilities = Array.from(
        document.querySelectorAll('input[name="utilities"]:checked'),
        checkbox => checkbox.value
    );

    const sortBy = document.querySelector('#sortBy').value || '';

    return {
        keyword, cityId, districtId, schoolType, admissionAge,
        minFee, maxFee, facilities, utilities, sortBy
    };
}

// Create URL parameters from filter values
function createParams(filterValues) {
    const params = new URLSearchParams();

    // Add basic parameters if they exist
    if (filterValues.keyword) params.append('keyword', filterValues.keyword);
    if (filterValues.cityId) params.append('cityId', filterValues.cityId);
    if (filterValues.districtId) params.append('districtId', filterValues.districtId);
    if (filterValues.schoolType) params.append('schoolType', filterValues.schoolType);
    if (filterValues.admissionAge) params.append('admissionAge', filterValues.admissionAge);
    if (filterValues.minFee) params.append('minFee', filterValues.minFee);
    if (filterValues.maxFee) params.append('maxFee', filterValues.maxFee);
    if (filterValues.page !== undefined) params.append('page', filterValues.page);

    // Add array parameters
    filterValues.facilities.forEach(facility => params.append('facilities', facility));
    filterValues.utilities.forEach(utility => params.append('utilities', utility));

    // Add sort parameter
    if (filterValues.sortBy) params.append('sortBy', filterValues.sortBy);

    return params;
}

// Navigate to a specific page with current filters
function navigateToPage(page) {
    const filterValues = getFilterValues();
    filterValues.page = page;

    // Show loading indicator
    const resultsContainer = document.querySelector('#search-content');
    if (resultsContainer) {
        resultsContainer.innerHTML = '<div class="text-center p-5"><i class="fas fa-spinner fa-spin fa-2x"></i><p class="mt-2">Loading results...</p></div>';
    }

    // Create URL parameters
    const params = createParams(filterValues);

    // Update URL without page reload


    // Fetch API data
    fetchResults(`/api/search-results?${params.toString()}`);
}

// Main filter submission function - always starts at page 0
function submitFilters() {
    navigateToPage(0);
}

// Function for the sort dropdown
function submitFiltersWithPage() {
    const currentPage = new URLSearchParams(window.location.search).get('page') || 0;
    navigateToPage(parseInt(currentPage));
}

// Fetch search results from API
function fetchResults(apiUrl) {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            updateResults(data.schools, data.facilities);
            updateResultCount(data.totalElements);
            updatePagination(data.currentPage, data.totalPages);
        })
        .catch(error => {
            console.error('Error fetching search results:', error);
            const resultsContainer = document.querySelector('#search-content');
            if (resultsContainer) {
                resultsContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Error loading results. Please try again later.
                    </div>
                `;
            }
        });
}

// Update the search results in the DOM
function updateResults(schools, facilitiesMap) {
    const resultsContainer = document.querySelector('#search-content');
    if (!resultsContainer) return;

    if (schools && schools.length > 0) {
        resultsContainer.innerHTML = '';
        schools.forEach(school => {
            const schoolCard = createSchoolCard(school, facilitiesMap);
            resultsContainer.appendChild(schoolCard);
        });
    } else {
        resultsContainer.innerHTML = `
            <div class="alert alert-info" role="alert">
                No schools found matching your criteria. Try adjusting your filters.
            </div>
        `;
    }
}

// Update the result count display
function updateResultCount(totalElements) {
    const countElement = document.querySelector('#messages .result-message span:nth-of-type(2)');
    if (countElement && totalElements !== undefined) {
        countElement.textContent = totalElements;
    }
}

// Create a school card element
function createSchoolCard(school, facilitiesMap) {
    const card = document.createElement('div');
    card.className = 'card school-card';
    card.style = 'overflow-y: auto; height: 350px';

    // Create facilities HTML
    let facilitiesHTML = '';
    if (facilitiesMap && facilitiesMap[school.schoolId]) {
        facilitiesHTML = facilitiesMap[school.schoolId]
            .map(facility => `<span class="facility-badge">${facility}</span>`)
            .join('');
    } else {
        facilitiesHTML = '<span>No facilities available</span>';
    }

    // Create rating stars HTML
    const starsHTML = generateStarsHTML(school.avgRating);

    card.innerHTML = `
        <div class="row g-0">
            <div class="col-md-4">
                <div class="img-school">
                    <img src="${school.schoolImage || '/user_side/images/school-image/school-placeholder.png'}" 
                         class="img-fluid school-image" 
                         alt="School view"
                         onerror="this.onerror=null;this.src='/user_side/images/school-image/school-placeholder.png';">
                </div>
            </div>
            <div class="col-md-8">
                <div class="card-body p-4">
                    <div class="d-flex mb-3" style="display: flex;justify-content: space-between;align-items: center">
                        <a href="/public/school/details/${school.schoolId}">
                            <h3 class="card-title school-title"><span>${school.schoolName}</span></h3>
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
                                ${starsHTML}
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
                            ${facilitiesHTML}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    return card;
}

// Format numbers with commas for readability
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// Generate HTML for star ratings
function generateStarsHTML(rating) {
    let html = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            html += '<span class="star-filled">★</span>';
        } else if (i <= rating + 0.5) {
            html += '<span class="star-half">★</span>';
        } else {
            html += '<span class="star-empty">★</span>';
        }
    }
    return html;
}

// Update pagination controls
function updatePagination(currentPage, totalPages) {
    const paginationContainer = document.querySelector('.pagination');
    if (!paginationContainer) return;

    let paginationHTML = '';

    // Previous button
    paginationHTML += `
        <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
        </li>
    `;

    // Show first page
    paginationHTML += `
        <li class="page-item ${currentPage == 0 ? 'active' : ''}">
            <a class="page-link" href="#" data-page="0">1</a>
        </li>
    `;

    // Show ellipsis if needed
    if (currentPage > 2) {
        paginationHTML += `
            <li class="page-item disabled">
                <span class="page-link">...</span>
            </li>
        `;
    }

    // Show page before current if available
    if (currentPage > 0) {
        paginationHTML += `
            <li class="page-item">
                <a class="page-link" href="#" data-page="${currentPage - 1}">${currentPage}</a>
            </li>
        `;
    }

    // Show current page if not first or last
    if (currentPage > 0 && currentPage < totalPages - 1) {
        paginationHTML += `
            <li class="page-item active">
                <a class="page-link" href="#" data-page="${currentPage}">${currentPage + 1}</a>
            </li>
        `;
    }

    // Show page after current if available
    if (currentPage < totalPages - 2) {
        paginationHTML += `
            <li class="page-item">
                <a class="page-link" href="#" data-page="${currentPage + 1}">${currentPage + 2}</a>
            </li>
        `;
    }

    // Show ellipsis if needed
    if (currentPage < totalPages - 3) {
        paginationHTML += `
            <li class="page-item disabled">
                <span class="page-link">...</span>
            </li>
        `;
    }

    // Show last page if not first
    if (totalPages > 1) {
        paginationHTML += `
            <li class="page-item ${currentPage == totalPages - 1 ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${totalPages - 1}">${totalPages}</a>
            </li>
        `;
    }

    // Next button
    paginationHTML += `
        <li class="page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
        </li>
    `;

    paginationContainer.innerHTML = paginationHTML;
}

// Clear all filters
function clearFilters() {
    // Reset search input
    const searchInput = document.querySelector('input[placeholder="Enter a school name"]');
    if (searchInput) searchInput.value = '';

    // Reset select dropdowns
    const selects = ['select[name="schoolType"]', 'select[name="admissionAge"]', '#sortBy'];
    selects.forEach(selector => {
        const select = document.querySelector(selector);
        if (select) select.selectedIndex = 0;
    });

    // Reset city and district
    const citySelect = document.querySelector('#citySelect');
    const districtSelect = document.querySelector('#district');
    if (citySelect) citySelect.selectedIndex = 0;
    if (districtSelect) {
        districtSelect.innerHTML = '<option value="0" disabled selected>Please choose city/province</option>';
    }

    // Reset range sliders
    const rangeMin = document.querySelector('.range-min');
    const rangeMax = document.querySelector('.range-max');
    const inputMin = document.querySelector('.input-min');
    const inputMax = document.querySelector('.input-max');

    if (rangeMin && rangeMax && inputMin && inputMax) {
        rangeMin.value = rangeMin.min;
        rangeMax.value = rangeMax.max;
        inputMin.value = rangeMin.min;
        inputMax.value = rangeMax.max;
        updateProgress();
    }

    // Uncheck all checkboxes (facilities and utilities)
    const checkboxSelectors = ['input[name="facilities"]', 'input[name="utilities"]'];
    checkboxSelectors.forEach(selector => {
        document.querySelectorAll(selector).forEach(checkbox => {
            checkbox.checked = false;
        });
    });

    // Apply the cleared filters
    submitFilters();
}

// Initialize all event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Set up event listener for pagination using event delegation
    const paginationContainer = document.querySelector('.pagination');
    if (paginationContainer) {
        paginationContainer.addEventListener('click', function(e) {
            if (e.target.classList.contains('page-link') && !e.target.parentElement.classList.contains('disabled')) {
                e.preventDefault();
                const page = e.target.getAttribute('data-page');
                if (page !== null) {
                    navigateToPage(parseInt(page));
                }
            }
        });
    }

    // Setup debounced filter inputs
    const debouncedSubmit = debounce(submitFilters, 300);

    // Text inputs
    const textInputs = ['input[placeholder="Enter a school name"]', 'input[name="minFee"]', 'input[name="maxFee"]'];
    textInputs.forEach(selector => {
        const input = document.querySelector(selector);
        if (input) input.addEventListener('input', debouncedSubmit);
    });

    // Select inputs
    const selectInputs = ['#citySelect', '#district', 'select[name="schoolType"]', 'select[name="admissionAge"]', '#sortBy'];
    selectInputs.forEach(selector => {
        const select = document.querySelector(selector);
        if (select) select.addEventListener('change', debouncedSubmit);
    });

    // Checkboxes (facilities and utilities)
    const checkboxSelectors = ['input[name="facilities"]', 'input[name="utilities"]'];
    checkboxSelectors.forEach(selector => {
        document.querySelectorAll(selector).forEach(checkbox => {
            checkbox.addEventListener('change', debouncedSubmit);
        });
    });

    // Button handlers
    const applyBtn = document.querySelector('#applyFilterBtn');
    if (applyBtn) applyBtn.addEventListener('click', submitFilters);

    const clearBtn = document.querySelector('#clearFilterBtn');
    if (clearBtn) clearBtn.addEventListener('click', clearFilters);
});