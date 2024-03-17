import { useEffect, useState } from 'react';

interface QuizSingleTimerProps {
  onTimerEndEvent: Function;
  timeLeft: number;
  remainingTimeInSeconds: number;
}

// The component that displays a the timer of a single quiz
export default function QuizSingleTimer(props: QuizSingleTimerProps) {
  // Counter in seconds
  const [counter, setCounter] = useState(Math.round(props.timeLeft * 60));

  const minutes = Math.floor(counter / 60);
  const seconds = counter - minutes * 60;

  useEffect(() => {
    let timeout: NodeJS.Timeout;

    if (counter > 0) {
      // Every second deduct 1 from the counter
      timeout = setTimeout(() => setCounter(counter - 1), 1000);
    } else {
      // Signalize to parent component that the time has ran out
      props.onTimerEndEvent();
    }

    // Clear setTimeout on component unmount
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
