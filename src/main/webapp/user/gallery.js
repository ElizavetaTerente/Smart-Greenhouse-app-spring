const output = document.getElementById("imageOutput");
const input = document.getElementById("imageInput");
let imagesArray = [];

input.addEventListener("change", () => {
    const files = input.files;
    for (let i=0; i < files.length; i++){
        imagesArray.push(files[i]);
    }
    displayImages();
})

function displayImages(){
    let images = "";
    imagesArray.forEach((image, index) => {
        images += `<div class="image">
                <img src="${URL.createObjectURL(image)}" alt="image">
                <span style="color: red" onclick="deleteImage(${index})">&times;</span>
              </div>`
    })
    output.innerHTML = images
}

function deleteImage(index){
    imagesArray.splice(index, 1)
    displayImages();
}
