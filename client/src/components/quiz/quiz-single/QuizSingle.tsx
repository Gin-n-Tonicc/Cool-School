import { useState } from 'react';
import './QuizSingle.scss';

export default function QuizSingle() {
  const [selectedAnswer, setSelectedAnswer] = useState(-1);

  const mockedAnswers = [...new Array(4)].map((x, i) => ({
    id: i + 1,
    text: 'Number in Math',
  }));

  return (
    <section className="quiz-parent">
      <div className="display-container">
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
        <div className="question-container">
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
        <div className="quiz-arrow-buttons">
          <button className="next-button">
            <i className="zmdi zmdi-arrow-left"></i>
          </button>
          <button className="next-button">
            <i className="zmdi zmdi-arrow-right"></i>
          </button>
        </div>
      </div>
    </section>
  );
}
