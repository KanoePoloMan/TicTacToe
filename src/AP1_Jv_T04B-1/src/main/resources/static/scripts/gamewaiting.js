const searchURL = 'http://localhost:8081/game/multiplayer/checkInFoundedList';
const gameURL = 'http://localhost:8081/game/multiplayer/';

let searchTimerId = setInterval(sendSearchRequest, 1000); 
var gameUUID = null;

async function sendSearchRequest() {
    let response = await fetch(searchURL);
    gameUUID = await response.text();
    
    if(gameUUID != '') {
        console.log(gameUUID);

        clearInterval(searchTimerId);
        location.href = gameURL + gameUUID;
    }
}
