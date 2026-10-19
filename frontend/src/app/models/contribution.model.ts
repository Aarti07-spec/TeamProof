export interface MemberContribution {
  userId: number;
  fullName: string;
  email: string;
  role: string;
  assignedTasksCount: number;
  completedTasksCount: number;
  totalEstimatedHours: number;
  completedEstimatedHours: number;
  submissionsCount: number;
  approvedSubmissionsCount: number;
}

export interface ContributionResponse {
  projectId: number | string;
  disclaimer: string;
  contributions: MemberContribution[];
}
