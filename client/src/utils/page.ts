export function validatePage(maxPages: number, page: number) {
  if (page > maxPages) {
    return maxPages;
  }

  if (page < 1) {
    return 1;
  }

  return page;
}
