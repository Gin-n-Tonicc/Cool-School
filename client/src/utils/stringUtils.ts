// Util file that helps us handle strings

export function camelCaseToWords(s: string) {
  const result = s.replace(/([A-Z])/g, ' $1');
  return result.charAt(0).toUpperCase() + result.slice(1);
}

export function capitalizeWord(s: string) {
  return s[0].toUpperCase() + s.slice(1).toLowerCase();
}
