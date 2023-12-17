// custom-dropdown.js

document.addEventListener("click", function (e) {
    closeAllSelect(e.target);
});

function closeAllSelect(elmnt) {
    const items = document.getElementsByClassName("select-items");
    const selects = document.getElementsByClassName("select-selected");

    for (const item of items) {
        if (elmnt !== item && elmnt !== selects[0]) {
            item.classList.add("select-hide");
        }
    }
}

const selects = document.getElementsByClassName("select-selected");
for (const select of selects) {
    select.addEventListener("click", function (e) {
        e.stopPropagation();
        closeAllSelect(this);
        this.nextSibling.classList.toggle("select-hide");
    });
}

const options = document.getElementsByClassName("option");
for (const option of options) {
    option.addEventListener("click", function (e) {
        const selectedText = this.innerText;
        const select = this.parentNode.parentNode.getElementsByClassName("select-selected")[0];
        select.innerText = selectedText;
    });
}
