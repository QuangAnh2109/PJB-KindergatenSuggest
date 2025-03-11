var starDictionary2x = {
    oneStar: "<i class=\"fa fa-star star-orange fa-2x\"></i>",
    halfStar: "<i class=\"fa fa-star-half-o star-orange fa-2x\"></i>",
    noStar: "<i class=\"fa fa-star-o star-orange fa-2x\"></i>"
};
var starDictionary = {
    oneStar: "<i class=\"fa fa-star star-orange \"></i>",
    halfStar: "<i class=\"fa fa-star-half-o star-orange \"></i>",
    noStar: "<i class=\"fa fa-star-o star-orange \"></i>"
};

function buildStarRating(score, isLarge = false) {
    let starsHTML = "";
    let dict = isLarge ? starDictionary2x : starDictionary;

    let fullStars = Math.floor(score / 10);
    let halfStar = (score % 10) === 5;
    let emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

    for (let i = 0; i < fullStars; i++) {
        starsHTML += dict.oneStar;
    }
    if (halfStar) {
        starsHTML += dict.halfStar;
    }
    for (let i = 0; i < emptyStars; i++) {
        starsHTML += dict.noStar;
    }

    return starsHTML;
}