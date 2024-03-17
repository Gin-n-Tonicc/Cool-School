// Util file that helps us handle date

// Calculate time since given date
export function timeSince(
  date: Date,
  divisors: { [key: number]: string },
  defaultUnit: string
) {
  // Sort divisor from highest to smallest (years, months, hours, etc.)
  const divisorEntries = Object.entries(divisors).sort(
    ([divisorA, _A], [divisorB, _B]) => Number(divisorB) - Number(divisorA)
  );

  const seconds = Math.floor(new Date().getTime() - date.getTime()) / 1000;

  // Calculate interval of each divisor
  for (const [divisor, description] of divisorEntries) {
    const interval = seconds / Number(divisor);

    if (interval >= 1) {
      return Math.floor(interval) + ` ${description}`;
    }
  }

  return Math.floor(seconds) + ` ${defaultUnit}`;
}
