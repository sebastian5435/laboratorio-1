import { useCallback, useEffect, useState } from 'react'
import { createCar, getCoolCars, updateCar, type Car, type CarInput } from './api'
import CarForm from './CarForm'
import GiphyImage from './GiphyImage'

const priceFormat = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 })

const CarList = () => {
  const [cars, setCars] = useState<Car[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editingId, setEditingId] = useState<number | null>(null)

  const loadCars = useCallback(async () => {
    try {
      setCars(await getCoolCars())
      setError(null)
    } catch {
      setError('No se pudo conectar con la API. ¿Está corriendo el backend en http://localhost:8080?')
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    loadCars()
  }, [loadCars])

  const handleCreate = async (car: CarInput) => {
    await createCar(car)
    await loadCars()
  }

  const handleUpdate = (id: number) => async (car: CarInput) => {
    await updateCar(id, car)
    setEditingId(null)
    await loadCars()
  }

  if (isLoading) {
    return <p>Cargando...</p>
  }

  return (
    <main className="content">
      <section className="panel">
        <h2>Nuevo carro (POST)</h2>
        <CarForm onSave={handleCreate} />
      </section>

      <section>
        <h2>Lista de carros</h2>
        {error && <p className="error">{error}</p>}
        <div className="grid">
          {cars.map((car) => (
            <article className="card" key={car.id}>
              {editingId === car.id ? (
                <>
                  <h3>Editar (PUT)</h3>
                  <CarForm car={car} onSave={handleUpdate(car.id)} onCancel={() => setEditingId(null)} />
                </>
              ) : (
                <>
                  <GiphyImage name={car.name} />
                  <h3>{car.name}</h3>
                  <dl>
                    <dt>Modelo</dt>
                    <dd>{car.model ?? '—'}</dd>
                    <dt>Color</dt>
                    <dd>{car.color ?? '—'}</dd>
                    <dt>Año</dt>
                    <dd>{car.year ?? '—'}</dd>
                    <dt>Precio</dt>
                    <dd>{car.price == null ? '—' : priceFormat.format(car.price)}</dd>
                  </dl>
                  <button className="secondary" onClick={() => setEditingId(car.id)}>
                    Editar
                  </button>
                </>
              )}
            </article>
          ))}
        </div>
      </section>
    </main>
  )
}

export default CarList
