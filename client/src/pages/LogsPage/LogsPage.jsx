import React, { useState } from "react";
import Button from '@mui/material/Button';
import axios from 'axios'
import default_host from "../../config/config"
import Snackbar from '@mui/material/Snackbar';
import Cookies from 'js-cookie';
import { DataGrid } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom'

const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'login', headerName: 'Логин', width: 130 },
    { field: 'body', headerName: 'Запрос', width: 600 },
];

const rows = [
    { id: 1, login: 'Snow', body: 'Запрос: анау аыуаываывываыва', age: 35 },
    { id: 2, login: 'Lannister', body: 'Cersei', age: 42 },
    { id: 3, login: 'Lannister', body: 'Jaime', age: 45 },
    { id: 4, login: 'Stark', body: 'Arya', age: 16 },
    { id: 5, login: 'Targaryen', body: 'Daenerys', age: null },
    { id: 6, login: 'Melisandre', body: null, age: 150 },
    { id: 7, login: 'Clifford', body: 'Ferrara', age: 44 },
    { id: 8, login: 'Frances', body: 'Rossini', age: 36 },
    { id: 9, login: 'Roxie', body: 'Harvey', age: 65 },
];
const Logs = () => {
    const navigate = useNavigate()

    return (
        <>
            <div style={{ width: '80%', margin: '0 auto', marginTop: '100px'}}>
                <h1>
                    Логи
                </h1>
            </div>
            <div style={{ width: '80%', margin: '0 auto', marginTop: '50px'}}>
                <DataGrid
                    rows={rows}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: { page: 0, pageSize: 5 },
                        },
                    }}
                    pageSizeOptions={[5, 10]}
                    // checkboxSelection
                />

            </div>
        </>
    );
};

export default Logs;