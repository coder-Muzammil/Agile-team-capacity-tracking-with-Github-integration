import React, { useEffect, useState } from "react";

const Dashboard = () => {
  const [pullRequests, setPullRequests] = useState([]);
  const [commits, setCommits] = useState([]);
  const [issues, setIssues] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/pull-requests")
      .then((res) => res.json())
      .then((data) => setPullRequests(data));

    fetch("http://localhost:8080/api/commits")
      .then((res) => res.json())
      .then((data) => setCommits(data));

    fetch("http://localhost:8080/api/issues")
      .then((res) => res.json())
      .then((data) => setIssues(data));
  }, []);

  return (
    <div>
      <h1>Agile Team Dashboard</h1>
      <h2>Pull Requests</h2>
      <ul>{pullRequests.map((pr, index) => <li key={index}>{pr}</li>)}</ul>
      <h2>Commits</h2>
      <ul>{commits.map((commit, index) => <li key={index}>{commit}</li>)}</ul>
      <h2>Issues</h2>
      <ul>{issues.map((issue, index) => <li key={index}>{issue}</li>)}</ul>
    </div>
  );
};

export default Dashboard;
