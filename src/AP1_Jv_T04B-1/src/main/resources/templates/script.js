const gameBoard = [
  ['', '', ''],
  ['', '', ''],
  ['', '', '']
];

let currentPlayer = 'X';
let gameOver = false;
let winningLine = [];

const gameContainer = document.getElementById('game-container');
const gameResult = document.getElementById('game-result'); // Получаем элемент для отображения результата

let backendGame = {
  uuid: null,
  gameField: {
    gameField: [
      [0, 0, 0],
      [0, 0, 0],
      [0, 0, 0]
    ]
  },
  error: null
};

const newGameURL = 'http://localhost:8080/game/newGame';
let updateGameURL = 'http://localhost:8080/game/';

function createGameBoard() {
  for (let i = 0; i < 3; i++) {
    const row = document.createElement('div');
    row.classList.add('row');
    for (let j = 0; j < 3; j++) {
      const cell = document.createElement('div');
      cell.classList.add('cell');
      cell.dataset.row = i;
      cell.dataset.col = j;
      cell.addEventListener('click', handleCellClick);
      row.appendChild(cell);
    }
    gameContainer.appendChild(row);
  }
  getNewGame();
  if(backendGame.error != null) alert(backendGame.error);
}

async function handleCellClick(event) {
  if (gameOver) return;

  const row = parseInt(event.target.dataset.row);
  const col = parseInt(event.target.dataset.col);

  if (gameBoard[row][col] !== '') return;

  gameBoard[row][col] = currentPlayer;
  event.target.textContent = currentPlayer;
  backendGame.gameField.gameField[row][col] = currentPlayer === 'X' ? 2 : 1;
  await updateGame();
  renderGameBoard();

  if (checkWin() != '') {
    gameOver = true;
    highlightWinningLine(); // Подсветка победной линии
    gameResult.innerHTML = `${checkWin()} победил!`; // Выводим результат в gameResult
  } else if (checkDraw()) {
    gameOver = true;
    gameResult.innerHTML = 'Ничья!';
  } else {
    // switchPlayer();
  }
}

function checkWin() {
  // Проверка строк
  for (let i = 0; i < 3; i++) {
    if (gameBoard[i][0] !== '' && gameBoard[i][0] === gameBoard[i][1] && gameBoard[i][0] === gameBoard[i][2]) {
      winningLine = [[i, 0], [i, 1], [i, 2]]; // Запись координат победной линии
      if(gameBoard[i][0] == 'X') return 'X';
      else return 'O';
    }
  }

  // Проверка столбцов
  for (let i = 0; i < 3; i++) {
    if (gameBoard[0][i] !== '' && gameBoard[0][i] === gameBoard[1][i] && gameBoard[0][i] === gameBoard[2][i]) {
      winningLine = [[0, i], [1, i], [2, i]];
      if(gameBoard[0][i] == 'X') return 'X';
      else return 'O';
    }
  }

  // Проверка диагоналей
  if (gameBoard[0][0] !== '' && gameBoard[0][0] === gameBoard[1][1] && gameBoard[0][0] === gameBoard[2][2]) {
    winningLine = [[0, 0], [1, 1], [2, 2]];
    if(gameBoard[0][0] == 'X') return 'X';
      else return 'O';
  }
  if (gameBoard[0][2] !== '' && gameBoard[0][2] === gameBoard[1][1] && gameBoard[0][2] === gameBoard[2][0]) {
    winningLine = [[0, 2], [1, 1], [2, 0]];
    if(gameBoard[0][2] == 'X') return 'X';
      else return 'O';
  }

  return '';
}

function checkDraw() {
  for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
      if (gameBoard[i][j] === '') {
        return false;
      }
    }
  }
  return true;
}

function switchPlayer() {
  currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
}

function highlightWinningLine() {
  winningLine.forEach(([row, col]) => {
    const cell = gameContainer.querySelectorAll('.cell')[row * 3 + col];
    cell.style.backgroundColor = 'red'; // Закрашиваем ячейку победной линии красным
  });
}

createGameBoard();

const themeSwitch = document.getElementById('theme-switch');

themeSwitch.addEventListener('click', () => {
  document.body.classList.toggle('dark');
  document.querySelector('.container').classList.toggle('dark');
  document.querySelectorAll('.cell').forEach(cell => cell.classList.toggle('dark'));
  document.getElementById('game-result').classList.toggle('dark');
});

async function getNewGame() {
  if(backendGame.uuid == null) {
    let response = await fetch(newGameURL);
    backendGame = await response.json();
    console.log(backendGame);

    renderGameBoard();
  }
}
async function updateGame() {
  if(backendGame.error == null) {
    let response = await fetch(updateGameURL + backendGame.uuid, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(backendGame.gameField)
    });
    console.log("Sended: " + JSON.stringify(backendGame.gameField));
    backendGame = await response.json();
    console.log(backendGame);
  } else {
    alert(backendGame.error);
  }
}
function renderGameBoard() {
  for(let row = 0; row < 3; row++) {
    for(let col = 0; col < 3; col++) {
      const cell = gameContainer.querySelectorAll('.cell')[row * 3 + col];
      if(backendGame.gameField.gameField[row][col] == 1) {
        gameBoard[row][col] = 'O';
        cell.textContent = 'O';
      } else if(backendGame.gameField.gameField[row][col] == 2) {
        gameBoard[row][col] = 'X';
        cell.textContent = 'X';
      }
    }
  }
}