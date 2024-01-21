export function timeSince(
  date: Date,
  divisors: { [key: number]: string },
  defaultUnit: string
) {
  const divisorEntries = Object.entries(divisors).sort(
    ([divisorA, _A], [divisorB, _B]) => Number(divisorB) - Number(divisorA)
  );

  const seconds = Math.floor(new Date().getTime() - date.getTime()) / 1000;

  for (const [divisor, description] of divisorEntries) {
    const interval = seconds / Number(divisor);

    if (interval >= 1) {
      return Math.floor(interval) + ` ${description}`;
    }
  }

  return Math.floor(seconds) + ` ${defaultUnit}`;
}
