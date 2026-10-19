export interface Task {
  id?: number;
  projectId: number;
  assignedTo?: number | null;
  title: string;
  description?: string;
  estimatedHours: number;
  priority: string;
  status: string;
  deadline?: string;
  createdAt?: string;
  updatedAt?: string;
}
