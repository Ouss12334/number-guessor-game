var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/guess', function (match) {
            showMatch(JSON.parse(match.body).correspondence);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = $("#name");
    stompClient.send("/app/hello", {}, JSON.stringify({'name': name.val()}));
    name.val('')
}

function sendGuess() {
    var guess = $("#guess")
    stompClient.send("/app/guess", {}, JSON.stringify({'number': guess.val()}));
    guess.val('')
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
    $("#form-hello").hide();
    $("#form-guess").show();
}

function showMatch(message) {
    $("#guess-match").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#send-guess" ).click(function() { sendGuess(); });
    $('#guess').on("input drop", function() {
        if (!this.value.match(/^\d{0,4}$/) || this.value.split("").some(function(v,i,a){
            return (a.lastIndexOf(v)!=i)})) {
            console.log("replacing invalid/duplicate char")
            $("#guess").val(this.value.replace(/.$/, ''));
        }
    })
    $("#form-guess").hide();
});