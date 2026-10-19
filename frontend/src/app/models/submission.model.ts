export interface Submission {
  id?: number;
  taskId: number;
  submittedBy: number;
  description: string;
  evidenceUrl?: string;
  filePath?: string;
  fileName?: string;
  status: string; // 'PENDING' | 'APPROVED' | 'CHANGES_REQUESTED'
  submittedAt?: string;
}
