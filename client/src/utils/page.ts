// Util file that helps us handle pagination

// Make sure page is (p >= 1 && page <= max)
export function validatePage(maxPages: number, page: number) {
  if (page > maxPages) {
    return maxPages;
  }

  if (page < 1) {
    return 1;
  }

  return page;
}

// Make sure index is in bounds of the length
export function validateIndex(length: number, index: number) {
  if (index >= length) {
    return length - 1;
  }

  if (index < 0) {
    return 0;
  }

  return index;
}
