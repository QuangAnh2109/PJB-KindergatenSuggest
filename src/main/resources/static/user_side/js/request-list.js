document.addEventListener("DOMContentLoaded", function() {
    // Get pagination elements
    const paginationLinks = document.querySelectorAll('.pagination .page-link');
    const totalPages = parseInt(document.querySelector('[data-total-pages]').getAttribute('data-total-pages'));

    // Create loading overlay element
    const loadingOverlay = document.createElement('div');
    loadingOverlay.className = 'loading-overlay';
    loadingOverlay.innerHTML = `
        <div class="loading-spinner">
            <i class="fa fa-spinner fa-spin fa-3x"></i>
            <p>Loading ...</p>
        </div>
    `;
    document.body.appendChild(loadingOverlay);

    // Add styles for the loading overlay
    const style = document.createElement('style');
    style.textContent = `
        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(255, 255, 255, 0.7);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }
        .loading-spinner {
            text-align: center;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .loading-spinner p {
            margin-top: 10px;
            font-weight: bold;
        }
    `;
    document.head.appendChild(style);

    // Add click event listeners to pagination links
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            let pageNumber;
            if (this.textContent === '«') {
                // Previous page
                const activePage = document.querySelector('.pagination .active');
                pageNumber = parseInt(activePage.textContent) - 2; // -1 for previous, -1 for zero-based indexing
                if (pageNumber < 0) return;
            } else if (this.textContent === '»') {
                // Next page
                const activePage = document.querySelector('.pagination .active');
                pageNumber = parseInt(activePage.textContent); // No need to subtract 1 as we're already getting the next page
                if (pageNumber >= totalPages) return;
            } else {
                // Specific page number
                pageNumber = parseInt(this.textContent) - 1; // Convert to zero-based index
            }

            // Show loading overlay
            showLoadingOverlay();

            // Call function to load page data
            loadRequestsPage(pageNumber);
        });
    });

    // Function to show loading overlay
    function showLoadingOverlay() {
        loadingOverlay.style.display = 'flex';
    }

    // Function to hide loading overlay with delay
    function hideLoadingOverlay() {
        // Add a 1-second delay before hiding the loading overlay
        setTimeout(() => {
            loadingOverlay.style.display = 'none';
        }, 300);
    }

    // Function to load page data via AJAX
    function loadRequestsPage(page) {
        // Show loading indicator in the container
        const requestContainer = document.getElementById('requestContainer');
        requestContainer.innerHTML = '<div class="text-center"><i class="fa fa-spinner fa-spin fa-3x"></i><p>Loading...</p></div>';

        // Make AJAX request
        fetch(`/api/my-request?page=${page}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => response.json())
            .then(data => {
                // Update container with new data
                requestContainer.innerHTML = '';

                // Update request count
                document.querySelector('#messages span').textContent = data.totalElements;

                // Generate HTML for each request
                data.content.forEach(request => {
                    const requestHTML = generateRequestHTML(request);
                    requestContainer.innerHTML += requestHTML;
                });

                // Update pagination active state
                updatePaginationState(page, data.totalPages);

                // Re-initialize any event listeners for the new content
                initializeContentListeners();

                // Hide loading overlay with delay
                hideLoadingOverlay();
            })
            .catch(error => {
                console.error('Error loading page data:', error);
                requestContainer.innerHTML = '<div class="alert alert-danger">Error loading requests. Please try again.</div>';

                // Hide loading overlay even on error (with delay)
                hideLoadingOverlay();
            });
    }

    // Helper function to sanitize user input
    function escapeHTML(unsafeText) {
        if (!unsafeText) return '';
        const div = document.createElement('div');
        div.textContent = unsafeText;
        return div.innerHTML;
    }

    // Helper function to format date
    function formatDate(dateString) {
        if (!dateString) return '';
        try {
            // Parse the date from the server
            const date = new Date(dateString);

            // Format the date as DD/MM/YYYY HH:MM
            const day = date.getDate().toString().padStart(2, '0');
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const year = date.getFullYear();
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');

            return `${day}/${month}/${year} ${hours}:${minutes}`;
        } catch (e) {
            console.error('Error formatting date:', e);
            return escapeHTML(dateString); // Return escaped original if parsing fails
        }
    }

    // Function to generate HTML for a request
    function generateRequestHTML(request) {
        const status = escapeHTML(request.requestMasterName);
        const statusClass = status === 'Closed' ? 'status-closed' : 'status-open';
        const rating = parseFloat(request.avgRating) || 0;

        // Generate stars HTML
        let starsHTML = '';
        for (let i = 1; i <= 5; i++) {
            let starClass = i <= rating ? 'star-filled' : (i <= rating + 0.5 ? 'star-half' : 'star-empty');
            starsHTML += `<span class="${starClass}">★</span>`;
        }

        return `
            <div class="row request-column">
                <div class="col-md-7">
                    <div class="request-card">
                        <div style="overflow-y: auto; height: 270px">
                            <div style="display: flex;justify-content: space-between">
                                <div style="margin-bottom: 10px" class="status-time">${formatDate(request.createTime)}</div>
                                <div>
                                    <span class="${statusClass}">${status}</span>
                                </div>
                            </div>
                            <div>
                                <h5>Request Number: #${escapeHTML(String(request.id))}</h5>
                            </div>
                            <p><strong>Request Information:</strong></p>
                            <p>Full Name: ${escapeHTML(request.fullName)}</p>
                            <p>Email address: ${escapeHTML(request.requestEmail)}</p>
                            <p>Phone number: ${escapeHTML(request.requestPhone)}</p>
                            <p class="content" id="textContent-${escapeHTML(String(request.id))}">
                                ${escapeHTML(request.inquires)}
                            </p>
                            <a class="see-more" id="seeMoreBtn-${escapeHTML(String(request.id))}" onclick="toggleContent('${escapeHTML(String(request.id))}')">See more...</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="school-summary">
                        <h5>School Summary</h5>
                        <h3><a href="/public/school/${escapeHTML(String(request.schoolId))}">${escapeHTML(request.requestSchoolName)}</a></h3>
                        <p><i class="fa-solid fa-location-dot"></i> Address: ${escapeHTML(request.address)}</p>
                        <p><i class="fa-solid fa-envelope"></i> Email: ${escapeHTML(request.emailSchool)}</p>
                        <p>
                            <i class="fa-solid fa-money-bills"></i>
                            Tuition fee: From ${typeof request.feeFrom === 'number' ? request.feeFrom.toLocaleString() : escapeHTML(String(request.feeFrom))} VND/month
                        </p>
                        <p><i class="fa-solid fa-book-bookmark"></i> Admission age: From ${escapeHTML(String(request.ageRange))}</p>
                        <div class="star-rating">
                            ${starsHTML}
                            <span>${escapeHTML((Math.ceil(request.avgRating * 2) / 2).toFixed(1))}</span>/5
                            (<span>${escapeHTML(String(request.totalRating))}</span> ratings)
                        </div>
                    </div>
                </div>
                <p style="font-size: 13px;font-style: italic;color: darkgrey"> *Our staff will contact you within 24
                    hrs. If you need urgent assistance, please contact us via our
                    hotline 0123456789</p>
            </div>
        `;
    }

    // Function to update pagination active state
    function updatePaginationState(currentPage, totalPages) {
        const pagination = document.querySelector('.pagination');
        let paginationHTML = '';

        // Previous button
        paginationHTML += `<li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#">«</a>
        </li>`;

        // Page numbers
        for (let i = 0; i < totalPages; i++) {
            paginationHTML += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#">${i + 1}</a>
            </li>`;
        }

        // Next button
        paginationHTML += `<li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#">»</a>
        </li>`;

        pagination.innerHTML = paginationHTML;

        // Re-add event listeners to pagination links
        document.querySelectorAll('.pagination .page-link').forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();

                let pageNumber;
                if (this.textContent === '«') {
                    pageNumber = currentPage - 1;
                    if (pageNumber < 0) return;
                } else if (this.textContent === '»') {
                    pageNumber = currentPage + 1;
                    if (pageNumber >= totalPages) return;
                } else {
                    pageNumber = parseInt(this.textContent) - 1;
                }

                // Show loading overlay before loading new page
                showLoadingOverlay();

                loadRequestsPage(pageNumber);
            });
        });
    }

    // Function to initialize event listeners for dynamic content
    function initializeContentListeners() {
        // Check for content overflow and show "See more" buttons
        document.querySelectorAll('.content').forEach(content => {
            const id = content.id.split('-')[1];
            const seeMoreBtn = document.getElementById(`seeMoreBtn-${id}`);
            if (!seeMoreBtn) return;

            const computedStyle = window.getComputedStyle(content);
            const lineHeight = parseFloat(computedStyle.lineHeight);
            const maxHeight = lineHeight * 3; // 3 lines max

            if (content.scrollHeight > maxHeight) {
                seeMoreBtn.style.display = "inline"; // Show "See more..."
            } else {
                seeMoreBtn.style.display = "none"; // Hide "See more..."
            }
        });
    }
});

// Update the existing toggleContent function to work with dynamic content
function toggleContent(id) {
    // Make sure id is sanitized before using it in selectors
    id = id.replace(/[^\w-]/g, '');

    var content = document.getElementById(`textContent-${id}`);
    var seeMoreBtn = document.getElementById(`seeMoreBtn-${id}`);

    if (content.classList.contains("expanded")) {
        content.classList.remove("expanded");
        content.style.webkitLineClamp = "3";
        content.style.overflow = "hidden";
        seeMoreBtn.textContent = "See more...";
    } else {
        content.classList.add("expanded");
        content.style.webkitLineClamp = "unset";
        content.style.overflow = "visible";
        seeMoreBtn.textContent = "See less";
    }
}