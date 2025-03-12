
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




