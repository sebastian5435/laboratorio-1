export interface Car {
  id: number
  name: string
  model: string | null
  color: string | null
  year: number | null
  price: number | null
}

export type CarInput = Omit<Car, 'id'>

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(API_URL + path, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...init?.headers },
  })
  if (!response.ok) {
    throw new Error(`Error ${response.status} al llamar ${path}`)
  }
  return response.json() as Promise<T>
}

export const getCoolCars = () => request<Car[]>('/cool-cars')

export const createCar = (car: CarInput) =>
  request<Car>('/cars', { method: 'POST', body: JSON.stringify(car) })

export const updateCar = (id: number, car: CarInput) =>
  request<Car>(`/cars/${id}`, { method: 'PUT', body: JSON.stringify(car) })
