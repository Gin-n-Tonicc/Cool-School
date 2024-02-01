import { useState } from 'react';
import './Quiz.scss';

export default function Quiz() {
  const [selectedAnswer, setSelectedAnswer] = useState(-1);

  const mockedAnswers = [...new Array(4)].map((x, i) => ({
    id: i + 1,
    text: 'Number in Math',
  }));

  return (
    <section className="quiz-parent container section_padding">
      <div id="display-container">
        <div className="header">
          <div className="number-of-count">
            <span className="number-of-question">1 of 3 questions</span>
          </div>
          <div className="timer-div">
            <img
              src="https://uxwing.com/wp-content/themes/uxwing/download/time-and-date/stopwatch-icon.png"
              width={20}
            />
            <span className="time-left">10s</span>
          </div>
        </div>
        <div id="container">
          <div className="container-mid">
            <p className="question">Google (www.google.com) is a:</p>
            {mockedAnswers.map((x) => (
              <button
                className={
                  'option-div' + (selectedAnswer === x.id ? ` selected` : '')
                }
                onClick={() =>
                  setSelectedAnswer((prev) => (prev !== x.id ? x.id : -1))
                }
                key={x.id}>
                {x.text}
              </button>
            ))}
          </div>
        </div>
        <button id="next-button">Next</button>
      </div>
    </section>
  );
}
