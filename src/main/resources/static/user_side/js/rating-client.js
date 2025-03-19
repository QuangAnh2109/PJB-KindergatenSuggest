// Initialize Bootstrap modal
let ratingModal;

document.addEventListener('DOMContentLoaded', function () {
    ratingModal = new bootstrap.Modal(document.getElementById('ratingModal'));

    // Initialize star rating functionality
    document.querySelectorAll('.star-rating').forEach(function (ratingContainer) {
        const stars = ratingContainer.querySelectorAll('.star');
        let isMouseDown = false;
        let startX = 0;
        let starRect = null;

        stars.forEach(function (star) {
            star.addEventListener('mousedown', function (e) {
                isMouseDown = true;
                starRect = star.getBoundingClientRect();
                startX = e.clientX;
                const value = parseInt(this.getAttribute('data-value'));
                const position = (e.clientX - starRect.left) / starRect.width;
                const rating = position <= 0.5 ? value - 0.5 : value;
                ratingContainer.setAttribute('data-rating', rating);
                updateStars(ratingContainer, rating);
            });

            star.addEventListener('mousemove', function (e) {
                if (isMouseDown) {
                    const value = parseInt(this.getAttribute('data-value'));
                    const position = (e.clientX - starRect.left) / starRect.width;
                    const rating = position <= 0.5 ? value - 0.5 : value;
                    ratingContainer.setAttribute('data-rating', rating);
                    updateStars(ratingContainer, rating);
                }
            });

            star.addEventListener('mouseover', function (e) {
                if (!isMouseDown) {
                    const value = parseInt(this.getAttribute('data-value'));
                    starRect = star.getBoundingClientRect();
                    const position = (e.clientX - starRect.left) / starRect.width;
                    const rating = position <= 0.5 ? value - 0.5 : value;
                    highlightStars(ratingContainer, rating);
                }
            });
        });

        document.addEventListener('mouseup', function () {
            isMouseDown = false;
        });

        ratingContainer.addEventListener('mouseleave', function () {
            if (!isMouseDown) {
                const currentRating = parseFloat(ratingContainer.getAttribute('data-rating'));
                highlightStars(ratingContainer, currentRating);
            }
        });
    });
});


// Function to open the rating modal with a specific schoolId
function openRatingModal(schoolId) {
    // Store the schoolId in a data attribute on the modal
    document.getElementById('ratingModal').setAttribute('data-school-id', schoolId);

    // Open the modal
    const ratingModal = new bootstrap.Modal(document.getElementById('ratingModal'));
    ratingModal.show();
}


function updateStars(container, value) {
    container.setAttribute('data-rating', value);
    highlightStars(container, value);

    // Update rating value display
    const ratingValue = container.closest('.d-flex').querySelector('.rating-value');
    ratingValue.textContent = value.toFixed(1);
}

function highlightStars(container, value) {
    const fullStars = Math.floor(value);
    const hasHalf = value % 1 !== 0;

    container.querySelectorAll('.star').forEach(function (star, index) {
        const starValue = parseInt(star.getAttribute('data-value'));
        if (starValue <= fullStars) {
            star.className = 'fa-solid fa-star star active';
        } else if (hasHalf && starValue === Math.ceil(value)) {
            star.className = 'fa-solid fa-star-half-stroke star active';
        } else {
            star.className = 'fa-regular fa-star star';
        }
    });

    // Update rating value display during hover
    const ratingValue = container.closest('.d-flex').querySelector('.rating-value');
    ratingValue.textContent = value.toFixed(1);
}

function submitRating() {
    let schoolId = document.getElementById('ratingModal').getAttribute('data-school-id');
    let feedback = document.getElementById('feedback').value.trim(); // Get feedback from textarea

    let data = {
        schoolId: schoolId,
        learningProgram: parseInt(document.querySelector('.rating-item:nth-child(1) .star-rating').getAttribute('data-rating')),
        facilities: parseInt(document.querySelector('.rating-item:nth-child(2) .star-rating').getAttribute('data-rating')),
        extracurricular: parseInt(document.querySelector('.rating-item:nth-child(3) .star-rating').getAttribute('data-rating')),
        teachers: parseInt(document.querySelector('.rating-item:nth-child(4) .star-rating').getAttribute('data-rating')),
        hygiene: parseInt(document.querySelector('.rating-item:nth-child(5) .star-rating').getAttribute('data-rating')),
        feedback: feedback
    };

    fetch('/api/create-feedback', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            alert("Thank you for your rating and feedback!");
        })
        .catch(error => console.error('Error:', error));
}
