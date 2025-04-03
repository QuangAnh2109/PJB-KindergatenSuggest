// Add this to the end of your feedback-list.js file or create a new tab-animation.js

document.addEventListener('DOMContentLoaded', function() {
    // Get all tab links
    const tabLinks = document.querySelectorAll('.nav-link[data-bs-toggle="tab"]');

    // Get tab content containers
    const overviewTab = document.getElementById('overview');
    const ratingsTab = document.getElementById('ratings');

    // Content placeholder for loading animation
    const loadingHtml = `
        <div class="text-center py-5 loading-animation">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2">Loading content...</p>
        </div>
    `;

    // Store original content
    let overviewContent = overviewTab.innerHTML;
    let ratingsContent = ratingsTab.innerHTML;

    // Add click event listeners to all tabs
    tabLinks.forEach(tab => {
        tab.addEventListener('click', function() {
            const targetTabId = this.getAttribute('href').substring(1);
            const targetTab = document.getElementById(targetTabId);

            // Show loading animation
            targetTab.innerHTML = loadingHtml;

            // Simulate loading delay (you can remove this in production)
            setTimeout(() => {
                // Restore original content
                if (targetTabId === 'overview') {
                    targetTab.innerHTML = overviewContent;
                } else if (targetTabId === 'ratings') {
                    targetTab.innerHTML = ratingsContent;

                    // Re-initialize ratings filters if needed
                    initializeRatingFilters();
                }

                // Fade in the content
                fadeInContent(targetTab);
            }, 500); // Adjust timing as needed
        });
    });

    // Handle direct navigation to ratings tab via URL hash
    if (window.location.hash === '#ratings') {
        // Initialize the ratings tab content
        setTimeout(() => {
            initializeRatingFilters();
        }, 100);
    }

    function fadeInContent(element) {
        // Add a CSS class for fade-in animation
        element.classList.add('fade-in-content');

        // Remove the class after animation completes
        setTimeout(() => {
            element.classList.remove('fade-in-content');
        }, 500);
    }

    function initializeRatingFilters() {
        // Get all filter buttons
        const filterButtons = document.querySelectorAll('.filter-btn');

        // Get the school ID from the page URL
        const pathParts = window.location.pathname.split('/');
        const schoolId = pathParts[pathParts.length - 1];

        // Re-bind click event listeners to filter buttons
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

        // Call API to load all feedback initially (filter=0 means all)
        fetchFilteredFeedback(schoolId, "0");
    }
});