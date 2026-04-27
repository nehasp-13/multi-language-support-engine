import { useEffect, useState } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";
import "./index.css";

function App() {
  const [data, setData] = useState([]);
  const [stats, setStats] = useState({ total: 0, english: 0 });

  const [title, setTitle] = useState("");
  const [language, setLanguage] = useState("");
  const [editIndex, setEditIndex] = useState(null);

  // FETCH DATA
  const fetchData = () => {
    fetch("http://localhost:8080/api/records")
      .then(res => res.json())
      .then(data => setData(data));
  };

  const fetchStats = () => {
    fetch("http://localhost:8080/api/records/stats")
      .then(res => res.json())
      .then(data => setStats(data));
  };

  useEffect(() => {
    fetchData();
    fetchStats();
  }, []);

  // ADD
  const handleAdd = () => {
    fetch("http://localhost:8080/api/records", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, language })
    }).then(() => {
      fetchData();
      fetchStats();
      setTitle("");
      setLanguage("");
    });
  };

  // DELETE
  const handleDelete = (index) => {
    fetch(`http://localhost:8080/api/records/${index}`, {
      method: "DELETE"
    }).then(() => {
      fetchData();
      fetchStats();
    });
  };

  // EDIT
  const handleEdit = (index, item) => {
    setEditIndex(index);
    setTitle(item.title);
    setLanguage(item.language);
  };

  // UPDATE
  const handleUpdate = () => {
    fetch(`http://localhost:8080/api/records/${editIndex}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, language })
    }).then(() => {
      setEditIndex(null);
      setTitle("");
      setLanguage("");
      fetchData();
      fetchStats();
    });
  };

  const chartData = [
    { name: "Total", value: stats.total },
    { name: "English", value: stats.english }
  ];

  return (
    <div className="container">
      
      <h1>Multi-Language Support Engine</h1>
      <h2>Day 6 — Dashboard 🚀</h2>

      {/* KPI CARDS */}
      <div className="cards">
  <div className="card">
    <h3>Total</h3>
    <p>{stats.total}</p>
  </div>

  <div className="card">
    <h3>English</h3>
    <p>{stats.english}</p>
  </div>
</div>

      {/* CHART */}
      <div className="chart">
        <BarChart width={400} height={300} data={chartData}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="value" fill="#4F46E5" />
        </BarChart>
      </div>

      {/* FORM */}
      <div className="form">
        <input
          placeholder="Enter Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <input
          placeholder="Enter Language"
          value={language}
          onChange={(e) => setLanguage(e.target.value)}
        />

        <button onClick={editIndex !== null ? handleUpdate : handleAdd}>
          {editIndex !== null ? "Update Record" : "Add Record"}
        </button>
      </div>

      <hr />

      {/* LIST */}
      <div className="list">
        {data.map((item, index) => (
          <div className="list-item" key={index}>
            <p>{item.title} - {item.language}</p>

            <button onClick={() => handleEdit(index, item)}>Edit</button>
            <button onClick={() => handleDelete(index)}>Delete</button>
          </div>
        ))}
      </div>

    </div>
  );
}

export default App;