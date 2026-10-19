import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from './services/task.service';
import { TeamService } from './services/team.service';
import { ProjectService } from './services/project.service';
import { UserService } from './services/user.service';
import { SubmissionService } from './services/submission.service';
import { ContributionService } from './services/contribution.service';
import { Task } from './models/task.model';
import { Team } from './models/team.model';
import { Project } from './models/project.model';
import { User } from './models/user.model';
import { Submission } from './models/submission.model';
import { ContributionResponse, MemberContribution } from './models/contribution.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  title = 'TeamProof — Group Project Contribution Tracker';
  activeTab: string = 'dashboard';

  // Data collections
  tasks: Task[] = [];
  teams: Team[] = [];
  projects: Project[] = [];
  users: User[] = [];
  submissions: Submission[] = [];
  contributionData: ContributionResponse | null = null;

  // Selected project for contribution view
  selectedContributionProjectId: number | null = null;

  // Loading states
  loadingTasks: boolean = false;
  loadingTeams: boolean = false;
  loadingProjects: boolean = false;
  loadingUsers: boolean = false;
  loadingSubmissions: boolean = false;
  loadingContributions: boolean = false;

  // Submission action states
  submittingTask: boolean = false;
  submittingTeam: boolean = false;
  submittingProject: boolean = false;
  submittingUser: boolean = false;
  submittingSubmission: boolean = false;
  reviewingSubmissionId: number | null = null;

  // Notifications
  errorMessage: string | null = null;
  successMessage: string | null = null;

  // Toggles for forms
  showTaskForm: boolean = false;
  showSubmissionForm: boolean = false;

  // Form Models
  newTask = {
    projectId: null as number | null,
    assignedTo: null as number | null,
    title: '',
    description: '',
    estimatedHours: 2.0,
    priority: 'MEDIUM',
    status: 'TODO',
    deadline: ''
  };

  newTeam = {
    name: '',
    description: ''
  };

  newProject = {
    teamId: null as number | null,
    name: '',
    description: '',
    status: 'ACTIVE'
  };

  newUser = {
    fullName: '',
    email: '',
    role: 'STUDENT'
  };

  newSubmission = {
    taskId: null as number | null,
    submittedBy: null as number | null,
    description: '',
    evidenceUrl: ''
  };

  constructor(
    private taskService: TaskService,
    private teamService: TeamService,
    private projectService: ProjectService,
    private userService: UserService,
    private submissionService: SubmissionService,
    private contributionService: ContributionService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loadTeams();
    this.loadProjects();
    this.loadTasks();
    this.loadUsers();
    this.loadSubmissions();
  }

  loadTasks(): void {
    this.loadingTasks = true;
    this.taskService.getTasks().subscribe({
      next: (data) => {
        this.tasks = data || [];
        this.loadingTasks = false;
      },
      error: (err) => {
        console.error('Error loading tasks:', err);
        this.errorMessage = 'Could not connect to Spring Boot backend at http://localhost:8080. Ensure the server is running.';
        this.loadingTasks = false;
      }
    });
  }

  loadTeams(): void {
    this.loadingTeams = true;
    this.teamService.getTeams().subscribe({
      next: (data) => {
        this.teams = data || [];
        this.loadingTeams = false;
      },
      error: (err) => {
        console.error('Error loading teams:', err);
        this.loadingTeams = false;
      }
    });
  }

  loadProjects(): void {
    this.loadingProjects = true;
    this.projectService.getProjects().subscribe({
      next: (data) => {
        this.projects = data || [];
        this.loadingProjects = false;
        // If no contribution project is selected yet, select the first one
        if (!this.selectedContributionProjectId && this.projects.length > 0) {
          this.selectedContributionProjectId = this.projects[0].id || null;
        }
      },
      error: (err) => {
        console.error('Error loading projects:', err);
        this.loadingProjects = false;
      }
    });
  }

  loadUsers(): void {
    this.loadingUsers = true;
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data || [];
        this.loadingUsers = false;
        if (!this.newSubmission.submittedBy && this.users.length > 0) {
          this.newSubmission.submittedBy = this.users[0].id || null;
        }
      },
      error: (err) => {
        console.error('Error loading users:', err);
        this.errorMessage = 'Could not load users from backend.';
        this.loadingUsers = false;
      }
    });
  }

  loadSubmissions(): void {
    this.loadingSubmissions = true;
    this.submissionService.getSubmissions().subscribe({
      next: (data) => {
        this.submissions = data || [];
        this.loadingSubmissions = false;
      },
      error: (err) => {
        console.error('Error loading submissions:', err);
        this.errorMessage = 'Could not load submissions from backend.';
        this.loadingSubmissions = false;
      }
    });
  }

  loadContributions(projectId?: number | null): void {
    this.loadingContributions = true;
    if (projectId) {
      this.contributionService.getProjectContributions(projectId).subscribe({
        next: (data) => {
          this.contributionData = data;
          this.loadingContributions = false;
        },
        error: (err) => {
          console.error('Error loading project contributions:', err);
          this.errorMessage = 'Could not load project contribution indicators.';
          this.loadingContributions = false;
        }
      });
    } else {
      this.contributionService.getAllContributions().subscribe({
        next: (data) => {
          this.contributionData = data;
          this.loadingContributions = false;
        },
        error: (err) => {
          console.error('Error loading all contributions:', err);
          this.errorMessage = 'Could not load contribution indicators.';
          this.loadingContributions = false;
        }
      });
    }
  }

  onSelectContributionProject(projectId: number | null): void {
    this.selectedContributionProjectId = projectId;
    this.loadContributions(projectId);
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    this.errorMessage = null;
    this.successMessage = null;
    if (tab === 'tasks') {
      this.loadTasks();
      this.loadProjects();
      this.loadUsers();
    } else if (tab === 'teams') {
      this.loadTeams();
    } else if (tab === 'projects') {
      this.loadProjects();
      this.loadTeams();
    } else if (tab === 'users') {
      this.loadUsers();
    } else if (tab === 'submissions') {
      this.loadSubmissions();
      this.loadTasks();
      this.loadUsers();
    } else if (tab === 'contributions') {
      this.loadProjects();
      this.loadTasks();
      if (!this.selectedContributionProjectId && this.projects.length > 0) {
        this.selectedContributionProjectId = this.projects[0].id || null;
      }
      this.loadContributions(this.selectedContributionProjectId);
    }
  }

  // Task Actions
  toggleTaskForm(): void {
    this.showTaskForm = !this.showTaskForm;
    this.errorMessage = null;
    this.successMessage = null;
  }

  onCreateTask(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newTask.title || this.newTask.title.trim() === '') {
      this.errorMessage = 'Task title cannot be empty.';
      return;
    }

    if (!this.newTask.projectId) {
      this.errorMessage = 'Please select a valid Project ID for this task.';
      return;
    }

    if (this.newTask.estimatedHours < 0) {
      this.errorMessage = 'Estimated hours cannot be negative.';
      return;
    }

    this.submittingTask = true;

    const payload: Partial<Task> = {
      projectId: Number(this.newTask.projectId),
      assignedTo: this.newTask.assignedTo ? Number(this.newTask.assignedTo) : null,
      title: this.newTask.title.trim(),
      description: this.newTask.description?.trim() || '',
      estimatedHours: Number(this.newTask.estimatedHours),
      priority: this.newTask.priority || 'MEDIUM',
      status: this.newTask.status || 'TODO',
      deadline: this.newTask.deadline ? this.newTask.deadline : undefined
    };

    this.taskService.createTask(payload).subscribe({
      next: (created) => {
        this.submittingTask = false;
        this.successMessage = `Task "#${created.id} - ${created.title}" created successfully!`;
        this.showTaskForm = false;
        // Reset form
        this.newTask = {
          projectId: this.projects.length > 0 ? (this.projects[0].id || null) : null,
          assignedTo: null,
          title: '',
          description: '',
          estimatedHours: 2.0,
          priority: 'MEDIUM',
          status: 'TODO',
          deadline: ''
        };
        this.loadTasks();
      },
      error: (err) => {
        console.error('Create task error:', err);
        this.submittingTask = false;
        this.errorMessage = err.error?.message || 'Failed to create task. Verify the Project ID exists in the database.';
      }
    });
  }

  // User Actions
  onCreateUser(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newUser.fullName || this.newUser.fullName.trim() === '') {
      this.errorMessage = 'Student/User full name is required.';
      return;
    }

    if (!this.newUser.email || this.newUser.email.trim() === '') {
      this.errorMessage = 'Email address is required.';
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(this.newUser.email.trim())) {
      this.errorMessage = 'Please enter a valid email address (e.g. alex@university.edu).';
      return;
    }

    this.submittingUser = true;

    const payload: Partial<User> = {
      fullName: this.newUser.fullName.trim(),
      email: this.newUser.email.trim().toLowerCase(),
      role: this.newUser.role || 'STUDENT'
    };

    this.userService.createUser(payload).subscribe({
      next: (created) => {
        this.submittingUser = false;
        this.successMessage = `User "${created.fullName}" (ID #${created.id}) registered successfully with role ${created.role}!`;
        this.newUser = {
          fullName: '',
          email: '',
          role: 'STUDENT'
        };
        this.loadUsers();
      },
      error: (err) => {
        console.error('Create user error:', err);
        this.submittingUser = false;
        this.errorMessage = err.error?.message || 'Failed to create user. Please ensure the email is valid and not already registered.';
      }
    });
  }

  // Team Actions
  onCreateTeam(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newTeam.name || this.newTeam.name.trim() === '') {
      this.errorMessage = 'Team name is required.';
      return;
    }

    this.submittingTeam = true;

    this.teamService.createTeam({
      name: this.newTeam.name.trim(),
      description: this.newTeam.description?.trim() || ''
    }).subscribe({
      next: (created) => {
        this.submittingTeam = false;
        this.successMessage = `Team "#${created.id} - ${created.name}" created successfully!`;
        this.newTeam = { name: '', description: '' };
        this.loadTeams();
      },
      error: (err) => {
        console.error('Create team error:', err);
        this.submittingTeam = false;
        this.errorMessage = 'Failed to create team. Ensure backend is running.';
      }
    });
  }

  // Project Actions
  onCreateProject(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newProject.name || this.newProject.name.trim() === '') {
      this.errorMessage = 'Project name is required.';
      return;
    }

    if (!this.newProject.teamId) {
      this.errorMessage = 'Please select a parent Team ID for this project.';
      return;
    }

    this.submittingProject = true;

    this.projectService.createProject({
      teamId: Number(this.newProject.teamId),
      name: this.newProject.name.trim(),
      description: this.newProject.description?.trim() || '',
      status: this.newProject.status || 'ACTIVE'
    }).subscribe({
      next: (created) => {
        this.submittingProject = false;
        this.successMessage = `Project "#${created.id} - ${created.name}" created successfully!`;
        this.newProject = {
          teamId: this.teams.length > 0 ? (this.teams[0].id || null) : null,
          name: '',
          description: '',
          status: 'ACTIVE'
        };
        this.loadProjects();
      },
      error: (err) => {
        console.error('Create project error:', err);
        this.submittingProject = false;
        this.errorMessage = err.error?.message || 'Failed to create project. Verify the Team ID exists in the database.';
      }
    });
  }

  // Submission Actions
  toggleSubmissionForm(): void {
    this.showSubmissionForm = !this.showSubmissionForm;
    this.errorMessage = null;
    this.successMessage = null;
  }

  onCreateSubmission(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.newSubmission.taskId) {
      this.errorMessage = 'Please select a Task for this submission.';
      return;
    }

    if (!this.newSubmission.submittedBy) {
      this.errorMessage = 'Please select the student submitter.';
      return;
    }

    if (!this.newSubmission.description || this.newSubmission.description.trim() === '') {
      this.errorMessage = 'Submission description / evidence notes cannot be empty.';
      return;
    }

    this.submittingSubmission = true;

    const payload: Partial<Submission> = {
      taskId: Number(this.newSubmission.taskId),
      submittedBy: Number(this.newSubmission.submittedBy),
      description: this.newSubmission.description.trim(),
      evidenceUrl: this.newSubmission.evidenceUrl ? this.newSubmission.evidenceUrl.trim() : ''
    };

    this.submissionService.createSubmission(payload).subscribe({
      next: (created) => {
        this.submittingSubmission = false;
        this.successMessage = `Evidence submission for Task #${created.taskId} recorded! Task moved to UNDER_REVIEW.`;
        this.showSubmissionForm = false;
        this.newSubmission = {
          taskId: this.tasks.length > 0 ? (this.tasks[0].id || null) : null,
          submittedBy: this.users.length > 0 ? (this.users[0].id || null) : null,
          description: '',
          evidenceUrl: ''
        };
        this.loadSubmissions();
        this.loadTasks();
      },
      error: (err) => {
        console.error('Create submission error:', err);
        this.submittingSubmission = false;
        this.errorMessage = err.error?.message || 'Failed to submit evidence. Verify Task and User exist.';
      }
    });
  }

  onReviewSubmission(submissionId: number, status: 'APPROVED' | 'CHANGES_REQUESTED'): void {
    this.errorMessage = null;
    this.successMessage = null;
    this.reviewingSubmissionId = submissionId;

    this.submissionService.reviewSubmission(submissionId, status).subscribe({
      next: (updated) => {
        this.reviewingSubmissionId = null;
        this.successMessage = `Submission #${submissionId} marked as ${updated.status}. Task status updated to ${updated.status === 'APPROVED' ? 'COMPLETED' : 'IN_PROGRESS'}!`;
        this.loadSubmissions();
        this.loadTasks();
        if (this.activeTab === 'contributions') {
          this.loadContributions(this.selectedContributionProjectId);
        }
      },
      error: (err) => {
        console.error('Review submission error:', err);
        this.reviewingSubmissionId = null;
        this.errorMessage = err.error?.message || 'Failed to update review status.';
      }
    });
  }

  // Lookup helpers
  getProjectName(projectId: number): string {
    const proj = this.projects.find(p => p.id === projectId);
    return proj ? proj.name : `Project #${projectId}`;
  }

  getTeamName(teamId: number): string {
    const team = this.teams.find(t => t.id === teamId);
    return team ? team.name : `Team #${teamId}`;
  }

  getUserDisplayName(userId?: number | null): string {
    if (!userId) return 'Unassigned';
    const user = this.users.find(u => u.id === userId);
    return user ? user.fullName : `User #${userId}`;
  }

  getTaskTitle(taskId: number): string {
    const task = this.tasks.find(t => t.id === taskId);
    return task ? task.title : `Task #${taskId}`;
  }

  // Metric Computations
  get totalTasks(): number {
    return this.tasks.length;
  }

  get completedTasks(): number {
    return this.tasks.filter(t => t.status?.toUpperCase() === 'COMPLETED').length;
  }

  get pendingReviews(): number {
    return this.tasks.filter(t => t.status?.toUpperCase() === 'UNDER_REVIEW').length;
  }

  get inProgressTasks(): number {
    return this.tasks.filter(t => t.status?.toUpperCase() === 'IN_PROGRESS').length;
  }

  get totalProjectsCount(): number {
    return this.projects.length;
  }

  get totalSubmissionsCount(): number {
    return this.submissions.length;
  }

  get approvedSubmissionsCount(): number {
    return this.submissions.filter(s => s.status?.toUpperCase() === 'APPROVED').length;
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'COMPLETED':
        return 'badge-completed';
      case 'UNDER_REVIEW':
        return 'badge-review';
      case 'IN_PROGRESS':
        return 'badge-progress';
      case 'TODO':
      default:
        return 'badge-todo';
    }
  }

  getSubmissionStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'APPROVED':
        return 'badge-completed';
      case 'CHANGES_REQUESTED':
        return 'priority-urgent';
      case 'PENDING':
      default:
        return 'badge-review';
    }
  }

  getPriorityClass(priority: string): string {
    switch (priority?.toUpperCase()) {
      case 'URGENT':
        return 'priority-urgent';
      case 'HIGH':
        return 'priority-high';
      case 'LOW':
        return 'priority-low';
      case 'MEDIUM':
      default:
        return 'priority-medium';
    }
  }

  getUserRoleBadgeClass(role?: string): string {
    switch (role?.toUpperCase()) {
      case 'ADMIN':
        return 'role-badge-admin';
      case 'TEAM_LEADER':
        return 'role-badge-leader';
      case 'STUDENT':
      default:
        return 'role-badge-student';
    }
  }
}
