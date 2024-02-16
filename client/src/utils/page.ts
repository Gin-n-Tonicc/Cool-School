export function validatePage(maxPages: number, page: number) {
  if (page > maxPages) {
    return maxPages;
  }

  if (page < 1) {
    return 1;
  }

  return page;
}

export function validateIndex(length: number, index: number) {
  if (index >= length) {
    return length - 1;
  }

  if (index < 0) {
    return 0;
  }

  return index;
}
