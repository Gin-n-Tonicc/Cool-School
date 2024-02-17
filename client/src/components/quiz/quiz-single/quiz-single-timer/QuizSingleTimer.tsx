import { useEffect, useState } from 'react';

interface QuizSingleTimerProps {
  onTimerEndEvent: Function;
  timeLeft: number;
  remainingTimeInSeconds: number;
}

export default function QuizSingleTimer(props: QuizSingleTimerProps) {
  const [counter, setCounter] = useState(Math.round(props.timeLeft * 60));

  const minutes = Math.floor(counter / 60);
  const seconds = counter - minutes * 60;

  useEffect(() => {
    let timeout: NodeJS.Timeout;

    if (counter > 0) {
      timeout = setTimeout(() => setCounter(counter - 1), 1000);
    } else {
      props.onTimerEndEvent();
      setCounter(10);
    }

    return () => {
      clearTimeout(timeout);
    };
  }, [counter]);

  return (
    <div className="timer-div">
      <img
        src="https://uxwing.com/wp-content/themes/uxwing/download/time-and-date/stopwatch-icon.png"
        width={20}
      />
      <span className="time-left">
        {minutes}m {seconds}s
      </span>
    </div>
  );
}
