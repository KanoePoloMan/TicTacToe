const getAvailableGamesURL = 'http://localhost:8081/game/multiplayer/getAvailableGames';
const getNameURL = 'http://localhost:8081/game/multiplayer/getNameByUUID';
const multiplayerURL = 'http://localhost:8081/game/multiplayer/';

// Данные для списка (можно заменить на ваши данные)
let listData = [];
let gamesList;

getAvailableList();

console.log("Games List");
console.log(gamesList);

  // Количество элементов списка (можно изменить)
let numberOfItems;

  // Получаем элемент списка по его ID
const scrollableList = document.getElementById("scrollableList");

  // Получаем кнопку "Вернуться в Меню" по ее ID
const backButton = document.getElementById("backButton");

// Функция для создания кнопки
function createListItemButton(text, index) {
    const button = document.createElement("button");
    button.classList.add("listItemButton"); // Добавляем класс для стилизации
    button.textContent = text;
    button.setAttribute("data-index", index); // Добавляем data-index атрибут
    button.addEventListener("click", onButtonClick);
    return button;
}

function onButtonClick() {
    const buttonIndex = this.getAttribute("data-index"); // Получаем data-index

    location.href = multiplayerURL + gamesList[buttonIndex].uuid + '/connectToGame';
}

// Обработчик события для кнопки "Вернуться в Меню"
backButton.addEventListener("click", function() {
    // Здесь добавьте код для возврата в меню.
    // Например, можно перенаправить пользователя на другую страницу:
    location.href = "http://localhost:8081/menu";
});

async function getAvailableList() {
    console.log("Get available games");
    let response = await fetch(getAvailableGamesURL);
    gamesList = await response.json();
    
    numberOfItems = gamesList.length;

    fillButtons();
}

async function fillButtons() {
    // Создаем и добавляем кнопки в список
    for (let i = 0; i < numberOfItems; i++) {
        let response = await fetch(getNameURL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(gamesList[i].x)
        });
        console.log("Sended: " + JSON.stringify(gamesList[i].x));
        
        let nick = await response.text()

        console.log(nick);

        listData[i] = nick;
        const button = createListItemButton(listData[i], i);
        scrollableList.appendChild(button);
    }
}
