document.addEventListener('DOMContentLoaded', function() {
    // Set up pagination click events
    setupPagination();
});

function setupPagination() {
    // Get all pagination links
    const paginationLinks = document.querySelectorAll('.pagination .page-link');

    // Add click event listener to each pagination link
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            let pageNumber;

            // Determine which page to load
            if (this.textContent === '«') {
                // Previous page
                const activePage = document.querySelector('.pagination .active');
                const currentPage = parseInt(activePage.querySelector('.page-link').textContent);
                pageNumber = Math.max(0, currentPage - 2); // -2 because we need 0-based and current is already +1
            } else if (this.textContent === '»') {
                // Next page
                const activePage = document.querySelector('.pagination .active');
                const currentPage = parseInt(activePage.querySelector('.page-link').textContent);
                const totalPages = parseInt(document.querySelector('[data-total-pages]').getAttribute('data-total-pages'));
                pageNumber = Math.min(totalPages - 1, currentPage); // Current already +1 from 0-based index
            } else {
                // Direct page number click
                pageNumber = parseInt(this.textContent) - 1; // Convert to 0-based for the controller
            }

            // Call function to load the selected page
            loadPage(pageNumber);
        });
    });
}

function loadPage(currentPage) {
    // Find the request container - using a more specific selector
    const requestContainer = document.querySelector('.container div[style*="overflow-y"]');

    if (!requestContainer) {
        console.error('Request container not found');
        return;
    }

    // Show a loading indicator
    const loadingIndicator = document.createElement('div');
    loadingIndicator.className = 'text-center p-5';
    loadingIndicator.innerHTML = '<i class="fas fa-spinner fa-spin fa-3x"></i><p class="mt-3">Loading requests...</p>';

    // Save current scroll position
    const scrollPosition = window.scrollY;

    // Store the original content
    const originalContent = requestContainer.innerHTML;

    // Clear current content and show loading
    requestContainer.innerHTML = '';
    requestContainer.appendChild(loadingIndicator);

    // Make AJAX request to get the new page of requests
    fetch(`/parent/my-request?currentPage=${currentPage}`, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html => {
            try {
                // Parse the HTML response
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');

                // Find the content in the response
                const responseContent = doc.querySelector('[data-total-pages]');

                if (!responseContent) {
                    throw new Error('Response doesn\'t contain the expected content');
                }

                // Get the parent element that contains all the request content
                const newRequestsContainer = responseContent.closest('div[style*="overflow-y"]');

                if (newRequestsContainer) {
                    // Update the content
                    requestContainer.innerHTML = newRequestsContainer.innerHTML;

                    // Update the total request count if available
                    const newRequestCount = doc.querySelector('#messages span');
                    if (newRequestCount) {
                        const currentRequestCount = document.querySelector('#messages span');
                        if (currentRequestCount) {
                            currentRequestCount.textContent = newRequestCount.textContent;
                        }
                    }

                    // Update pagination
                    const totalPagesElem = document.querySelector('[data-total-pages]');
                    if (totalPagesElem) {
                        updatePaginationUI(currentPage, totalPagesElem.getAttribute('data-total-pages'));
                    }

                    // Restore scroll position
                    window.scrollTo(0, scrollPosition);
                } else {
                    throw new Error('Could not find request container in response');
                }
            } catch (error) {
                console.error('Error processing response:', error);
                throw error; // Re-throw to be caught by the outer catch
            }
        })
        .catch(error => {
            console.error('Error fetching requests:', error);

            // Restore original content
            requestContainer.innerHTML = originalContent;

            // Show error message
            const errorAlert = document.createElement('div');
            errorAlert.className = 'alert alert-danger';
            errorAlert.textContent = 'Failed to load requests. Please try again.';
            requestContainer.prepend(errorAlert);

            // Auto-dismiss error after 5 seconds
            setTimeout(() => {
                if (errorAlert.parentNode) {
                    errorAlert.parentNode.removeChild(errorAlert);
                }
            }, 5000);
        });
}

function updatePaginationUI(currentPage, totalPages) {
    const pagination = document.querySelector('.pagination');

    if (!pagination) {
        console.error('Pagination container not found');
        return;
    }

    // Clear existing pagination
    pagination.innerHTML = '';

    // Add previous button
    const prevItem = document.createElement('li');
    prevItem.className = 'page-item' + (currentPage === 0 ? ' disabled' : '');
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = '#';
    prevLink.textContent = '«';
    prevItem.appendChild(prevLink);
    pagination.appendChild(prevItem);

    // Determine page range to show
    const totalPagesInt = parseInt(totalPages);
    const maxPagesToShow = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPagesInt - 1, startPage + maxPagesToShow - 1);

    // Adjust start if we're at the end
    if (endPage - startPage + 1 < maxPagesToShow) {
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    // Add page numbers
    for (let i = startPage; i <= endPage; i++) {
        const pageItem = document.createElement('li');
        pageItem.className = 'page-item' + (i === currentPage ? ' active' : '');

        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = '#';
        pageLink.textContent = (i + 1).toString(); // Display 1-based page numbers

        pageItem.appendChild(pageLink);
        pagination.appendChild(pageItem);
    }

    // Add next button
    const nextItem = document.createElement('li');
    nextItem.className = 'page-item' + (currentPage === totalPagesInt - 1 ? ' disabled' : '');
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = '#';
    nextLink.textContent = '»';
    nextItem.appendChild(nextLink);
    pagination.appendChild(nextItem);

    // Re-setup pagination event listeners
    setupPagination();
}