import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContributionResponse } from '../models/contribution.model';

@Injectable({
  providedIn: 'root'
})
export class ContributionService {
  private apiUrl = 'http://localhost:8080/api/contributions';

  constructor(private http: HttpClient) {}

  getProjectContributions(projectId: number): Observable<ContributionResponse> {
    return this.http.get<ContributionResponse>(`${this.apiUrl}/project/${projectId}`);
  }

  getAllContributions(): Observable<ContributionResponse> {
    return this.http.get<ContributionResponse>(this.apiUrl);
  }
}
