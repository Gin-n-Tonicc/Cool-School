const divisors = {
  31536000: 'years',
  2592000: 'months',
  86400: 'days',
  3600: 'hours',
  60: 'days',
};

const divisorEntries = Object.entries(divisors).sort(
  ([divisorA, _A], [divisorB, _B]) => Number(divisorB) - Number(divisorA)
);

export function timeSince(date: Date) {
  const seconds = Math.floor(new Date().getTime() - date.getTime()) / 1000;

  for (const [divisor, description] of divisorEntries) {
    const interval = seconds / Number(divisor);

    if (interval >= 1) {
      return Math.floor(interval) + ` ${description}`;
    }
  }

  return Math.floor(seconds) + ' seconds';
}
