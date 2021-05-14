function writer(text, duration) {
    let element = $("#label-guess");
    text = text || "guess?";
    duration = duration || 200;
    let len = text.length;
    let i = 0;
    let loop = setInterval(() => {
        if(i === len){
            clearInterval(loop)
        }
        else{
            element.append(text[i])
        }
        i++;
    }, duration);
}
