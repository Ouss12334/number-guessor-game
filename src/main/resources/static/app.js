let stompClient = null;

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     // if (connected) {
//     //     $(".table-conversation").show();
//     // }
//     // else {
//     //     $(".table-conversation").hide();
//     // }
//     $("#greetings").html("");
// }

function connect() {
    return new Promise(function(resolve, reject) {
        const socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            // setConnected(true);
            console.log('Connected: ' + frame);
            const sessionId = socket._transport.url.split('/')[5]
            stompClient.subscribe('/topic/greetings', function (greeting) {
                const players = JSON.parse(greeting.body);
                $("#greetings").empty();
                players.forEach(function (player) {
                    showGreeting(player.content)
                })
            });
            stompClient.subscribe('/topic/guess', function (score) {
                const parsedscore = JSON.parse(score.body);
                const isMe = parsedscore.sessionId === sessionId;

                if (parsedscore.number) {
                    $("#guess-match").empty();
                    let winnerMessage = '<b>' + parsedscore.username + '</b> won! he found the number <b>' + parsedscore.number + '</b>';
                    if (isMe)
                        winnerMessage = '<b>YOU</b> won! you found the magic number <b>' + parsedscore.number + '</b>'
                    showWin(winnerMessage, "a new game has started, <b>guess again!</b>", isMe);
                }
                else {
                    let match = parsedscore.correspondence;
                    if (match === '0')
                        match = 'nothing';
                    if (isMe)
                        parsedscore.username = 'You';
                    showMatch('<b>' + parsedscore.username + ':</b> ' + match, isMe);
                }
            });
            resolve();
        });
    })
}

// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }

function sendName() {
    let name = $("#name");
    stompClient.send("/app/hello", {}, JSON.stringify({'name': name.val()}));
    name.val('')
    $("#login").toggle();
    $("#main-content").toggle();
}

function sendGuess() {
    const guess = $("#guess");
    stompClient.send("/app/guess", {}, JSON.stringify({'number': guess.val()}));
    guess.val('')
}

function showGreeting(message) {
    // $("#greetings").append("<tr><td>" + message + "</td></tr>");
    $("#greetings").append("<li class=\"guests_history_list_item\">" + message + "</li>");
    // $("#form-hello").hide();
    // $("#form-guess").show();
}

function showMatch(message, isMe) {
    if (isMe === true)
        $("#guess-match").append("<li class=\"chatroom_history_list_item chatroom_history_list_left chatroom_history_list_item--blue\">" + message + "</li>");
    else
        $("#guess-match").append("<li class=\"chatroom_history_list_item chatroom_history_list_left chatroom_history_list_item--grey\">" + message + "</li>");
}

function showWin(messageWinner, messageReset, isMe) {
    if (isMe)
        $("#guess-match").append("<li class=\"chatroom_history_list_item chatroom_history_list_left chatroom_history_list_item--i_won\">" + messageWinner + "</li>");
    else
        $("#guess-match").append("<li class=\"chatroom_history_list_item chatroom_history_list_left chatroom_history_list_item--he_won\">" + messageWinner + "</li>");
    $("#guess-match").append("<li class=\"chatroom_history_list_item chatroom_history_list_left chatroom_history_list_item--reset\">" + messageReset + "</li>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { connect().then(sendName) });
    $( "#send-guess" ).click(function() { sendGuess(); });
    $('#guess').on("input drop", function() {
        if (!this.value.match(/^\d{0,4}$/) || this.value.split("").some(function(v,i,a){
            return (a.lastIndexOf(v)!=i)})) {
            // console.log("replacing invalid/duplicate char");
            $("#guess").val(this.value.replace(/.$/, ''));
        }
    });
    // $("#form-guess").hide();
    $("#main-content").hide();
    $("#login").toggle();
});