import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GraphqlService {
  private url = '/graphql';

  constructor(private http: HttpClient) {}

  query(query: string, variables?: any): Observable<any> {
    return this.http.post(this.url, { query, variables }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  mutate(mutation: string, variables?: any): Observable<any> {
    return this.http.post(this.url, { query: mutation, variables }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
}
