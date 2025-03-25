
// Function to get list Facilities and utilities selected to search
function getSelectedCheckboxValues() {
    function getSelectedValues(name) {
        return Array.from(document.querySelectorAll(`input[name="${name}"]:checked`))
            .map(checkbox => checkbox.value);
    }

    // Get values from both checkbox groups
    const selectedFacilities = getSelectedValues("facilities");
    const selectedUtilities = getSelectedValues("utilities");

    // Return both lists as an object
    return {
        facilities: selectedFacilities,
        utilities: selectedUtilities
    };
}


// JavaScript for price range slider
const rangeInput = document.querySelectorAll(".range-input input");
const priceInput = document.querySelectorAll(".price-input input");
const progress = document.querySelector(".slider .progress");
const priceGap = 1000;

// Initialize price range slider
rangeInput.forEach(input => {
    input.addEventListener("input", e => {
        let minVal = parseInt(rangeInput[0].value);
        let maxVal = parseInt(rangeInput[1].value);

        if(maxVal - minVal < priceGap) {
            if(e.target.className === "range-min") {
                rangeInput[0].value = maxVal - priceGap;
            } else {
                rangeInput[1].value = minVal + priceGap;
            }
        } else {
            priceInput[0].value = minVal;
            priceInput[1].value = maxVal;
            progress.style.left = (minVal / rangeInput[0].max) * 100 + "%";
            progress.style.right = 100 - (maxVal / rangeInput[1].max) * 100 + "%";
        }
    });
});

priceInput.forEach(input => {
    input.addEventListener("input", e => {
        let minVal = parseInt(priceInput[0].value);
        let maxVal = parseInt(priceInput[1].value);

        if((maxVal - minVal >= priceGap) && maxVal <= 1000000) {
            if(e.target.className === "input-min") {
                rangeInput[0].value = minVal;
                progress.style.left = (minVal / rangeInput[0].max) * 100 + "%";
            } else {
                rangeInput[1].value = maxVal;
                progress.style.right = 100 - (maxVal / rangeInput[1].max) * 100 + "%";
            }
        }
    });
});

// Apply filter button click handler
document.getElementById('applyFilterBtn').addEventListener('click', function() {
    const filterData = getFilterData();
    console.log("Filter data:", filterData);

    // You can add code here to send the data to your backend
    // Example: performSearch(filterData);
});

// Clear filter button click handler
document.getElementById('clearFilterBtn').addEventListener('click', function() {
    document.getElementById('filterForm').reset();

    // Reset range slider UI
    rangeInput[0].value = 0;
    rangeInput[1].value = 1000000;
    priceInput[0].value = 0;
    priceInput[1].value = 2000;
    progress.style.left = "0%";
    progress.style.right = "0%";

    console.log("Filters cleared");
});

// Function to collect all filter data
function getFilterData() {
    const form = document.getElementById('filterForm');
    const formData = new FormData(form);

    // Get selected facilities
    const selectedFacilities = [];
    document.querySelectorAll('input[name="facilities"]:checked').forEach(checkbox => {
        selectedFacilities.push(checkbox.value);
    });

    // Get selected utilities
    const selectedUtilities = [];
    document.querySelectorAll('input[name="utilities"]:checked').forEach(checkbox => {
        selectedUtilities.push(checkbox.value);
    });

    // Create filter data object
    const filterData = {
        schoolType: formData.get('schoolType') || null,
        admissionAge: formData.get('admissionAge') || null,
        priceRange: {
            min: parseInt(formData.get('minFee')) || 0,
            max: parseInt(formData.get('maxFee')) || 1000000
        },
        facilities: selectedFacilities,
        utilities: selectedUtilities
    };

    return filterData;
}

document.addEventListener('DOMContentLoaded', function() {
    const rangeMin = document.querySelector('.range-min');
    const rangeMax = document.querySelector('.range-max');
    const rangeSelected = document.querySelector('.range-selected');
    const minValueDisplay = document.getElementById('min-value');
    const maxValueDisplay = document.getElementById('max-value');

    // Function to convert values to millions format for display
    function formatValue(value) {
        return value + " million";
    }

    // Function to update the selected range visual
    function updateSelectedRange() {
        const minPercent = ((rangeMin.value - rangeMin.min) / (rangeMin.max - rangeMin.min)) * 100;
        const maxPercent = ((rangeMax.value - rangeMin.min) / (rangeMin.max - rangeMin.min)) * 100;

        rangeSelected.style.left = minPercent + '%';
        rangeSelected.style.width = (maxPercent - minPercent) + '%';

        minValueDisplay.textContent = formatValue(rangeMin.value);
        maxValueDisplay.textContent = formatValue(rangeMax.value);
    }

    // Initialize
    updateSelectedRange();

    // Set up event listeners
    rangeMin.addEventListener('input', function() {
        if (parseInt(rangeMin.value) > parseInt(rangeMax.value)) {
            rangeMin.value = rangeMax.value;
        }
        updateSelectedRange();
    });

    rangeMax.addEventListener('input', function() {
        if (parseInt(rangeMax.value) < parseInt(rangeMin.value)) {
            rangeMax.value = rangeMin.value;
        }
        updateSelectedRange();
    });
});




