export interface PaginationFilter<T> {
  page: number;
  size: number;
  sortBy?: keyof T;
  sortDir?: 'asc' | 'desc';
}

export interface PaginatedResult<T> {
  page: number;
  size: number;
  totalElements: number;
  elements: T[];
}