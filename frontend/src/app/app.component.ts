import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { GraphqlService } from './services/graphql.service';
import { AiService } from './services/ai.service';

interface Student {
  id: string;
  name: string;
  email: string;
  course: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, JsonPipe],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'GraphQL Angular - AI RAG Agent';

  // Student CRUD
  students: Student[] = [];
  studentForm = { id: '', name: '', email: '', course: '' };
  studentResult = '';

  // AI Chat
  chatMessage = '';
  chatResponse = '';

  // RAG
  ragQuestion = '';
  ragResponse = '';
  ragSources: string[] = [];

  // Agent
  agentTask = '';
  agentResponse = '';

  // Documents
  documents: any[] = [];
  docForm = { title: '', content: '', source: '' };

  // MCP
  mcpCapabilities: any = null;
  mcpTool = '';
  mcpResult = '';

  activeTab = 'students';
  loading = false;

  constructor(
    private gql: GraphqlService,
    private ai: AiService
  ) {}

  ngOnInit() {
    this.loadStudents();
  }

  // ===== Student CRUD =====
  loadStudents() {
    console.log('Loading students...');
    this.gql.query(`{ getStudents { id name email course } }`).subscribe({
      next: (res: any) => {
        console.log('Students loaded:', res);
        this.students = res?.data?.getStudents || [];
        this.studentResult = `Loaded ${this.students.length} students`;
      },
      error: (err: any) => {
        console.error('Load error:', err);
        this.studentResult = 'Error: ' + err.message;
      },
    });
  }

  createStudent() {
    const { name, email, course } = this.studentForm;
    if (!name || !email || !course) {
      this.studentResult = 'Please fill all fields';
      return;
    }
    this.studentResult = 'Creating...';
    this.gql
      .mutate(
        `mutation($n:String!,$e:String!,$c:String!){createStudent(name:$n,email:$e,course:$c){id name email course}}`,
        { n: name, e: email, c: course }
      )
      .subscribe({
        next: (res: any) => {
          console.log('Create response:', res);
          this.studentResult = 'Created! Refreshing list...';
          this.studentForm = { id: '', name: '', email: '', course: '' };
          this.loadStudents();
        },
        error: (err: any) => {
          console.error('Create error:', err);
          this.studentResult = 'Error: ' + err.message;
        },
      });
  }

  updateStudent() {
    const { id, name, email, course } = this.studentForm;
    this.gql
      .mutate(
        `mutation($id:ID!,$n:String!,$e:String!,$c:String!){updateStudent(id:$id,name:$n,email:$e,course:$c){id name email course}}`,
        { id, n: name, e: email, c: course }
      )
      .subscribe({
        next: () => {
          this.studentResult = 'Updated!';
          this.loadStudents();
        },
        error: (err: any) => (this.studentResult = 'Error: ' + err.message),
      });
  }

  deleteStudent(id: string) {
    this.gql
      .mutate(`mutation($id:ID!){deleteStudent(id:$id)}`, { id })
      .subscribe({
        next: () => {
          this.studentResult = 'Deleted!';
          this.loadStudents();
        },
        error: (err: any) => (this.studentResult = 'Error: ' + err.message),
      });
  }

  editStudent(s: Student) {
    this.studentForm = { ...s };
  }

  // ===== AI Chat =====
  sendChat() {
    this.loading = true;
    this.ai.chat(this.chatMessage).subscribe({
      next: (res: any) => {
        this.chatResponse = res.response;
        this.loading = false;
      },
      error: (err: any) => {
        this.chatResponse = 'Error: ' + err.message;
        this.loading = false;
      },
    });
  }

  // ===== RAG =====
  askRag() {
    this.loading = true;
    this.ai.rag(this.ragQuestion).subscribe({
      next: (res: any) => {
        this.ragResponse = res.response;
        this.ragSources = res.sources || [];
        this.loading = false;
      },
      error: (err: any) => {
        this.ragResponse = 'Error: ' + err.message;
        this.loading = false;
      },
    });
  }

  // ===== Agent =====
  runAgent() {
    this.loading = true;
    this.ai.agent(this.agentTask).subscribe({
      next: (res: any) => {
        this.agentResponse = res.response;
        this.loading = false;
      },
      error: (err: any) => {
        this.agentResponse = 'Error: ' + err.message;
        this.loading = false;
      },
    });
  }

  // ===== Documents =====
  loadDocuments() {
    this.gql.query(`{ getDocuments { id title content source } }`).subscribe({
      next: (res: any) => (this.documents = res.data.getDocuments || []),
      error: (err: any) => console.error(err),
    });
  }

  addDocument() {
    const { title, content, source } = this.docForm;
    this.gql
      .mutate(
        `mutation($t:String!,$c:String!,$s:String){addDocument(title:$t,content:$c,source:$s){id title content source}}`,
        { t: title, c: content, s: source || 'manual' }
      )
      .subscribe({
        next: () => {
          this.docForm = { title: '', content: '', source: '' };
          this.loadDocuments();
        },
        error: (err: any) => console.error(err),
      });
  }

  ingestDocuments() {
    this.gql.mutate(`mutation{ingestDocuments}`).subscribe({
      next: (res: any) => {
        alert(res.data.ingestDocuments);
        this.loadDocuments();
      },
    });
  }

  // ===== MCP =====
  loadMcpCapabilities() {
    this.ai.mcpCapabilities().subscribe({
      next: (res: any) => (this.mcpCapabilities = res),
      error: (err: any) => console.error(err),
    });
  }

  executeMcpTool() {
    this.loading = true;
    this.ai
      .mcpExecute({ tool: this.mcpTool, arguments: {} })
      .subscribe({
        next: (res: any) => {
          this.mcpResult = JSON.stringify(res, null, 2);
          this.loading = false;
          this.loadStudents();
        },
        error: (err: any) => {
          this.mcpResult = 'Error: ' + err.message;
          this.loading = false;
        },
      });
  }
}
