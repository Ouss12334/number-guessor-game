/**
 * Work-in-progress flocks simulation
 * https://codepen.io/aphiwadchhoeun/pen/jOBbozX
 */

let flocks = [];
let alignSlider = 0, cohesionSlider = 0, separationSlider = 0;

class Particle {
    constructor() {
        this.position = createVector(random(width), random(height))
        this.velocity = p5.Vector.random2D()
        this.velocity.setMag(random(2, 4));
        this.acceleration = createVector()
        this.maxForce = 0.3
        this.maxSpeed = 5
    }

    edges() {
        if (this.position.x < 0) {
            let reflect = createVector(this.maxSpeed, this.velocity.y)
            reflect.sub(this.velocity)
            reflect.limit(this.maxForce)
            this.acceleration.add(reflect)
        } else if (this.position.x > width) {
            let reflect = createVector(-this.maxSpeed, this.velocity.y)
            reflect.sub(this.velocity)
            reflect.limit(this.maxForce)
            this.acceleration.add(reflect)
        }
        if (this.position.y < 0) {
            let reflect = createVector(this.velocity.x, this.maxSpeed)
            reflect.sub(this.velocity)
            reflect.limit(this.maxForce)
            this.acceleration.add(reflect)
        } else if (this.position.y > height) {
            let reflect = createVector(this.velocity.x, -this.maxSpeed)
            reflect.sub(this.velocity)
            reflect.limit(this.maxForce)
            this.acceleration.add(reflect)
        }
    }

    align(flocks) {
        let senseRadius = 10
        let steering = createVector()
        let total = 0;

        for (let other of flocks) {
            if (other != this) {
                let d = this.position.dist(other.position)
                if (d <= senseRadius) {
                    steering.add(other.velocity)
                    total++
                }
            }
        }

        if (total > 0) {
            steering.div(total)
            steering.setMag(this.maxSpeed)
            steering.sub(this.velocity)
            steering.limit(this.maxForce)
        }

        return steering
    }

    cohesion(flocks) {
        let senseRadius = 50
        let steering = createVector()
        let total = 0

        // for(let other of flocks) {
        //   if (other != this) {
        //     let d = this.position.dist(other.position)
        //     if (d <= senseRadius) {
        //       steering.add(other.position)
        //       total++
        //     }
        //   }
        // }

        if (total > 0) {
            steering.div(total)
            steering.sub(this.position)
            steering.setMag(this.maxSpeed)
            steering.sub(this.velocity)
            steering.limit(this.maxForce)
        }

        return steering
    }

    separation(flocks) {
        let senseRadius = 15
        let steering = createVector()
        let total = 0

        for (let other of flocks) {
            if (other != this) {
                let d = this.position.dist(other.position)
                if (d <= senseRadius) {
                    let push = p5.Vector.sub(this.position, other.position)
                    push.div(d)

                    steering.add(push)
                    total++
                }
            }
        }

        if (total > 0) {
            steering.div(total)
            steering.setMag(this.maxSpeed)
            steering.sub(this.velocity)
            steering.limit(this.maxForce)
        }

        return steering
    }

    behavior(flocks) {
        let alignSteering = this.align(flocks)
        let cohesionSteering = this.cohesion(flocks)
        let separationSteering = this.separation(flocks)

        // alignSteering.mult(alignSlider.value())
        // cohesionSteering.mult(cohesionSlider.value())
        // separationSteering.mult(separationSlider.value())
        alignSteering.mult(alignSlider)
        cohesionSteering.mult(cohesionSlider)
        separationSteering.mult(separationSlider)

        this.acceleration.add(alignSteering)
        this.acceleration.add(cohesionSteering)
        this.acceleration.add(separationSteering)
    }

    update() {
        this.position.add(this.velocity)
        this.velocity.add(this.acceleration)
        this.velocity.limit(this.maxSpeed)
        this.acceleration.mult(0)
    }

    paint() {
        stroke('#9f5f80')
        fill('#ff8474')

        let angle = atan2(this.velocity.y, this.velocity.x)
        resetMatrix()
        translate(this.position.x, this.position.y)
        rotate(angle)
        quad(-15, 0, 0, -5, 5, 0, 0, 5)
    }
}

function setup() {
    createCanvas(windowWidth, windowHeight)
    // alignSlider = createSlider(0, 2, 1.2, 0.1);
    // cohesionSlider = createSlider(0, 2, 1.5, 0.1);
    // separationSlider = createSlider(0, 2, 1.8, 0.1);

    for (let i = 0; i < 150; i++) {
        flocks.push(new Particle())
    }
}

function draw() {
    //393e46
    background('#393e46')

    for (let p of flocks) {
        p.edges()
        p.behavior(flocks)
        p.update()
        p.paint()
    }
}

$(function () {
    setup();
    draw();
})