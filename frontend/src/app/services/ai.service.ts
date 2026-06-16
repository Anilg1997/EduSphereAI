import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AiService {
  private baseUrl = '/api/ai';
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  chat(message: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/chat`, { message }, { headers: this.headers });
  }

  rag(question: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/rag`, { message: question }, { headers: this.headers });
  }

  agent(task: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/agent`, { message: task }, { headers: this.headers });
  }

  mcpCapabilities(): Observable<any> {
    return this.http.get(`${this.baseUrl}/mcp/capabilities`);
  }

  mcpExecute(body: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/mcp/execute`, body, { headers: this.headers });
  }
}
